package com.ecobite.location_service.service;

import com.ecobite.location_service.DTO.*;
import com.ecobite.location_service.entity.BatchLocation;
import com.ecobite.location_service.entity.InventoryLocation;
import com.ecobite.location_service.exceptions.BadRequestException;
import com.ecobite.location_service.exceptions.ResourceNotFoundException;
import com.ecobite.location_service.feign.BatchClient;
import com.ecobite.location_service.repository.InventoryLocationRepository;
import com.ecobite.location_service.repository.LocationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        BatchLocation location = new BatchLocation();
        location.setWarehouse(request.getWarehouse());
        location.setSection(request.getSection());
        location.setShelf(request.getShelf());
        location.setCapacity(request.getCapacity());
        location.setCurrentOccupancy(0);
        location.setLocationCode(generateCode(request));
        location.setCreatedAt(LocalDateTime.now());

        locationRepo.save(location);

        return mapToResponse(location);
    }


    @Override
    @Transactional
    public void assignBatch(AssignBatchRequest request) {

        BatchLocation location = locationRepo.findById(request.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found"));

        // Validate batch from batch-service
        batchClient.getBatch(request.getBatchId());

        if (location.getCurrentOccupancy() + request.getQuantity() > location.getCapacity()) {
            throw new BadRequestException("Location capacity exceeded");
        }

        InventoryLocation inventory = new InventoryLocation();
        inventory.setBatchId(request.getBatchId());
        inventory.setLocationId(request.getLocationId());
        inventory.setQuantity(request.getQuantity());
        inventory.setAssignedAt(LocalDateTime.now());

        inventoryRepo.save(inventory);

        location.setCurrentOccupancy(location.getCurrentOccupancy() + request.getQuantity());
        locationRepo.save(location);
    }


    @Override
    @Transactional
    public void moveBatch(MoveBatchRequest request) {

        BatchLocation from = locationRepo.findById(request.getFromLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("From location not found"));

        BatchLocation to = locationRepo.findById(request.getToLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("To location not found"));

        InventoryLocation sourceInventory = inventoryRepo.findByBatchId(request.getBatchId())
                .stream()
                .filter(i -> i.getLocationId().equals(from.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Batch not in source location"));


        if (sourceInventory.getQuantity() < request.getQuantity()) {
            throw new BadRequestException("Insufficient quantity");
        }

        if (to.getCurrentOccupancy() + request.getQuantity() > to.getCapacity()) {
            throw new BadRequestException("Target location full");
        }

        int remainingQty = sourceInventory.getQuantity() - request.getQuantity();

        if (remainingQty == 0) {
            inventoryRepo.delete(sourceInventory); // clean DB
        } else {
            sourceInventory.setQuantity(remainingQty);
            inventoryRepo.save(sourceInventory);
        }

        InventoryLocation destinationInventory = inventoryRepo.findByBatchId(request.getBatchId())
                .stream()
                .filter(i -> i.getLocationId().equals(to.getId()))
                .findFirst()
                .orElse(null);

        if (destinationInventory != null) {
            destinationInventory.setQuantity(
                    destinationInventory.getQuantity() + request.getQuantity()
            );
            inventoryRepo.save(destinationInventory);
        } else {
            InventoryLocation newEntry = new InventoryLocation();
            newEntry.setBatchId(request.getBatchId());
            newEntry.setLocationId(to.getId());
            newEntry.setQuantity(request.getQuantity());
            newEntry.setAssignedAt(LocalDateTime.now());
            inventoryRepo.save(newEntry);
        }

        from.setCurrentOccupancy(from.getCurrentOccupancy() - request.getQuantity());
        to.setCurrentOccupancy(to.getCurrentOccupancy() + request.getQuantity());

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


    private String generateCode(CreateLocationRequest request) {
        return request.getWarehouse().toUpperCase() + "-"
                + request.getSection().toUpperCase() + "-"
                + request.getShelf().toUpperCase();
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
}
