package com.ecobite.location_service.service;

import org.springframework.transaction.annotation.Transactional;
import com.ecobite.location_service.DTO.*;
import com.ecobite.location_service.entity.BatchLocation;
import com.ecobite.location_service.entity.InventoryLocation;
import com.ecobite.location_service.exceptions.BadRequestException;
import com.ecobite.location_service.exceptions.ResourceNotFoundException;
import com.ecobite.location_service.feign.BatchClient;
import com.ecobite.location_service.repository.InventoryLocationRepository;
import com.ecobite.location_service.repository.LocationRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepo;
    private final InventoryLocationRepository inventoryRepo;
    private final BatchClient batchClient;

    @Override
    public LocationResponse createLocation(CreateLocationRequest request) {

        // Generate normalized location code
        String locationCode = generateCode(request);

        // Check duplicate location
        if (locationRepo.existsByLocationCode(locationCode)) {

            throw new BadRequestException(
                    "Location already exists"
            );
        }

        BatchLocation location = new BatchLocation();

        location.setWarehouse(
                request.getWarehouse().trim().toUpperCase()
        );

        location.setSection(
                request.getSection().trim().toUpperCase()
        );

        location.setShelf(
                request.getShelf().trim().toUpperCase()
        );

        location.setCapacity(request.getCapacity());
        location.setCurrentOccupancy(0);
        location.setLocationCode(locationCode);
        location.setCreatedAt(LocalDateTime.now());

        locationRepo.save(location);

        return mapToResponse(location);
    }


    @Override
    @Transactional
    public void assignBatch(AssignBatchRequest request) {
        BatchLocation location = locationRepo.findById(request.getLocationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Location not found"));

        BatchResponse batch;

        try {
            batch = batchClient.getBatch(request.getBatchId());
        } catch (FeignException.NotFound ex) {
            throw new ResourceNotFoundException("Batch not found");
        }

        if (request.getQuantity() <= 0) {

            throw new BadRequestException(
                    "Quantity must be greater than 0"
            );
        }

        validateBatchStatus(batch, "assign");

        // Total already assigned quantity
        Integer totalAssigned =
                inventoryRepo.getTotalAssignedQuantity(
                        request.getBatchId()
                );

        // Remaining available quantity
        int availableQuantity =
                batch.getRemainingQuantity() - totalAssigned;

        // Quantity validation
        if (request.getQuantity() > availableQuantity) {

            throw new BadRequestException(
                    "Requested quantity exceeds available batch quantity"
            );
        }

        // Location capacity validation
        if (location.getCurrentOccupancy() + request.getQuantity()
                > location.getCapacity()) {

            throw new BadRequestException(
                    "Location capacity exceeded"
            );
        }

        // Check if batch already exists in same location
        InventoryLocation inventory =
                inventoryRepo.findByBatchIdAndLocationId(
                        request.getBatchId(),
                        request.getLocationId()
                ).orElse(null);

        // If already exists → increase quantity
        if (inventory != null) {

            inventory.setQuantity(
                    inventory.getQuantity() + request.getQuantity()
            );

        } else {

            // Create new inventory entry
            inventory = new InventoryLocation();

            inventory.setBatchId(request.getBatchId());
            inventory.setLocationId(request.getLocationId());
            inventory.setQuantity(request.getQuantity());
            inventory.setAssignedAt(LocalDateTime.now());
        }

        // Save inventory
        inventoryRepo.save(inventory);

        location.setCurrentOccupancy(
                location.getCurrentOccupancy() + request.getQuantity()
        );

        locationRepo.save(location);

    }


    @Override
    @Transactional
    public void moveBatch(MoveBatchRequest request) {

        // Validate source location
        BatchLocation from = locationRepo.findById(request.getFromLocationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("From location not found"));

        // Validate destination location
        BatchLocation to = locationRepo.findById(request.getToLocationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("To location not found"));
        if (request.getQuantity() <= 0) {

            throw new BadRequestException(
                    "Quantity must be greater than 0"
            );
        }

        // Prevent moving to same location
        if (from.getId().equals(to.getId())) {
            throw new BadRequestException(
                    "Source and destination locations cannot be the same"
            );
        }

        // Validate batch from batch-service
        BatchResponse batch;

        try {
            batch = batchClient.getBatch(request.getBatchId());
        } catch (FeignException.NotFound ex) {
            throw new ResourceNotFoundException("Batch not found");
        }

        validateBatchStatus(batch, "move");

        // Find source inventory
        InventoryLocation sourceInventory =
                inventoryRepo.findByBatchIdAndLocationId(
                        request.getBatchId(),
                        from.getId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Batch not in source location"
                        ));

        // Quantity validation
        if (sourceInventory.getQuantity() < request.getQuantity()) {

            throw new BadRequestException(
                    "Insufficient quantity"
            );
        }

        // Destination capacity validation
        if (to.getCurrentOccupancy() + request.getQuantity()
                > to.getCapacity()) {

            throw new BadRequestException(
                    "Target location full"
            );
        }

        // Reduce quantity from source
        int remainingQty =
                sourceInventory.getQuantity() - request.getQuantity();

        if (remainingQty == 0) {

            // Remove source row completely
            inventoryRepo.delete(sourceInventory);

        } else {

            sourceInventory.setQuantity(remainingQty);
            inventoryRepo.save(sourceInventory);
        }

        // Check if batch already exists in destination
        InventoryLocation destinationInventory =
                inventoryRepo.findByBatchIdAndLocationId(
                        request.getBatchId(),
                        to.getId()
                ).orElse(null);

        // Merge quantities if exists
        if (destinationInventory != null) {

            destinationInventory.setQuantity(
                    destinationInventory.getQuantity()
                            + request.getQuantity()
            );

            inventoryRepo.save(destinationInventory);

        } else {

            // Create new inventory entry
            InventoryLocation newEntry = new InventoryLocation();

            newEntry.setBatchId(request.getBatchId());
            newEntry.setLocationId(to.getId());
            newEntry.setQuantity(request.getQuantity());
            newEntry.setAssignedAt(LocalDateTime.now());

            inventoryRepo.save(newEntry);
        }

        // Prevent negative occupancy
        if (from.getCurrentOccupancy() < request.getQuantity()) {

            throw new BadRequestException(
                    "Invalid occupancy update"
            );
        }
        // Update occupancy
        from.setCurrentOccupancy(
                from.getCurrentOccupancy() - request.getQuantity()
        );

        to.setCurrentOccupancy(
                to.getCurrentOccupancy() + request.getQuantity()
        );

        locationRepo.save(from);
        locationRepo.save(to);
    }


    @Override
    public List<LocationResponse> getAllLocations() {
        return locationRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<InventoryLocationResponse> getInventoryByLocation(Long locationId) {

        return inventoryRepo.findByLocationId(locationId)
                .stream()
                .map(this::mapToInventoryResponse)
                .toList();
    }

    @Override
    public List<InventoryLocationResponse> getLocationsByBatch(Long batchId) {

        return inventoryRepo.findByBatchId(batchId)
                .stream()
                .map(this::mapToInventoryResponse)
                .toList();
    }

    @Override
    public Long getWarehouseCount() {
        return locationRepo.countDistinctWarehouses();
    }

    @Override
    public LocationResponse getLocationById(Long id) {

        BatchLocation location =
                locationRepo.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Location not found"
                                ));

        return mapToResponse(location);
    }


    private String generateCode(CreateLocationRequest request) {

        String warehouse = request.getWarehouse() != null
                ? request.getWarehouse().trim().toUpperCase()
                : "UNKNOWN";

        String section = request.getSection() != null
                ? request.getSection().trim().toUpperCase()
                : "UNKNOWN";

        String shelf = request.getShelf() != null
                ? request.getShelf().trim().toUpperCase()
                : "UNKNOWN";

        return warehouse + "-" + section + "-" + shelf;
    }

    private LocationResponse mapToResponse(BatchLocation location) {
        LocationResponse res = new LocationResponse();
        res.setId(location.getId());
        res.setLocationCode(location.getLocationCode());
        res.setWarehouse(location.getWarehouse());
        res.setSection(location.getSection());
        res.setShelf(location.getShelf());
        res.setCapacity(location.getCapacity());
        res.setCurrentOccupancy(location.getCurrentOccupancy());
        return res;
    }

    private InventoryLocationResponse mapToInventoryResponse(InventoryLocation inv) {
        InventoryLocationResponse res = new InventoryLocationResponse();
        res.setBatchId(inv.getBatchId());
        res.setLocationId(inv.getLocationId());
        res.setQuantity(inv.getQuantity());
        return res;
    }

    private void validateBatchStatus(BatchResponse batch, String operation) {

        // Expiry validation
        if (batch.getExpiryDate() != null &&
                batch.getExpiryDate().isBefore(LocalDate.now())) {

            throw new BadRequestException(
                    "Cannot " + operation + " expired batch"
            );
        }

        String status = batch.getStatus();

        if (status == null) {
            return;
        }

        if ("RECALL".equalsIgnoreCase(status)) {

            throw new BadRequestException(
                    "Cannot " + operation + " recalled batch"
            );
        }

        if ("DAMAGED".equalsIgnoreCase(status)) {

            throw new BadRequestException(
                    "Cannot " + operation + " damaged batch"
            );
        }

        if ("EXPIRED".equalsIgnoreCase(status)) {

            throw new BadRequestException(
                    "Cannot " + operation + " expired batch"
            );
        }

        if ("QUARANTINED".equalsIgnoreCase(status)) {

            throw new BadRequestException(
                    "Cannot " + operation + " quarantined batch"
            );
        }

        if ("SPOILED".equalsIgnoreCase(status)) {

            throw new BadRequestException(
                    "Cannot " + operation + " spoiled batch"
            );
        }
    }
}
