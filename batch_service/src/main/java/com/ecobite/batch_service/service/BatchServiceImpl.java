package com.ecobite.batch_service.service;


import com.ecobite.batch_service.KafKaEventProducer.BatchEventProducer;
import com.ecobite.batch_service.KafKaEventProducer.DashboardEventProducer;
import com.ecobite.batch_service.dto.Kafkaevent.BatchEvent;
import com.ecobite.batch_service.dto.request.AllocateBatchRequest;
import com.ecobite.batch_service.dto.request.CreateBatchRequest;
import com.ecobite.batch_service.dto.request.ReduceStockRequest;
import com.ecobite.batch_service.dto.response.AvaliableStockResponse;
import com.ecobite.batch_service.dto.response.BatchAllocationResponse;
import com.ecobite.batch_service.dto.response.BatchResponse;
import com.ecobite.batch_service.dto.response.ProductResponse;
import com.ecobite.batch_service.entity.Batch;
import com.ecobite.batch_service.exception.ResourceNotFoundException;
import com.ecobite.batch_service.feign.LocationClient;
import com.ecobite.batch_service.feign.ProductClient;
import com.ecobite.batch_service.feign.SupplierClient;
import com.ecobite.batch_service.repository.BatchRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {
    private final BatchRepository repository;
    private final ProductClient productClient;
    private final BatchEventProducer producer;
    private final SupplierClient supplierClient;
    private final LocationClient locationClient;
    private final DashboardEventProducer   dashboardEventProducer;

    @Override
    @Transactional
    public BatchResponse createBatch(CreateBatchRequest request) {
        if (request.getExpiryDate().isBefore(request.getManufactureDate())) {
            throw new RuntimeException(
                    "Expiry date cannot be before manufacture date"
            );
        }

        //Prevent expired batch creation
        if (!request.getExpiryDate()
                .isAfter(LocalDate.now())) {

            throw new RuntimeException(
                    "Expiry date must be a future date"
            );
        }

        if (repository.existsByBatchNumber(request.getBatchNumber())) {
            throw new RuntimeException(
                    "Batch number already exists"
            );
        }
        if (request.getQuantity() <= 0) {
            throw new RuntimeException(
                    "Quantity must be greater than zero"
            );
        }

        if (request.getPurchasePrice().compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Purchase price must be greater than zero"
            );
        }

        ProductResponse product;

        try {

            product = productClient.getProduct(
                    request.getProductId()
            );

        } catch (feign.FeignException.NotFound ex) {

            throw new ResourceNotFoundException(
                    "Product not found"
            );

        } catch (feign.FeignException ex) {

            throw new RuntimeException(
                    "Product service unavailable"
            );
        }

        // Supplier validation
        try {

            supplierClient.getSupplier(
                    request.getSupplierId()
            );

        } catch (FeignException.NotFound ex) {

            throw new ResourceNotFoundException(
                    "Supplier not found"
            );

        } catch (FeignException ex) {

            throw new RuntimeException(
                    "Supplier service unavailable"
            );
        }

        // Location validation
        try {

            locationClient.getLocation(
                    request.getLocationId()
            );

        } catch (FeignException ex) {

            System.out.println("Location Feign Error Status: "
                    + ex.status());

            System.out.println("Location Feign Error Message: "
                    + ex.getMessage());

            if (ex.status() == 404) {

                throw new ResourceNotFoundException(
                        "Location not found"
                );
            }

            throw new RuntimeException(
                    "Location service unavailable"
            );
        }
        Batch batch = Batch.builder()
                .batchNumber(request.getBatchNumber())
                .productId(request.getProductId())
                .productName(product.getName())
                .supplierId(request.getSupplierId())
                .locationId(request.getLocationId())
                .quantity(request.getQuantity())
                .remainingQuantity(request.getQuantity())
                .manufactureDate(request.getManufactureDate())
                .expiryDate(request.getExpiryDate())
                .purchasePrice(request.getPurchasePrice())
                .status("ACTIVE")
                .build();

        repository.save(batch);

        int totalStock = calculateTotalStock(batch.getProductId());
        productClient.updateStock(
                batch.getProductId(),
                totalStock
        );

        BatchEvent event = new BatchEvent();
        event.setEventType("BATCH_CREATED");
        event.setProductName(product.getName());
        event.setBatchId(batch.getId());
        event.setExpiryDate(batch.getExpiryDate());

        producer.sendEvent(event);

        return mapToResponse(batch);
    }

    @Override
    public BatchResponse getBatchById(Long id) {

        Batch batch = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Batch not found"));

        return mapToResponse(batch);
    }

    @Override
    public List<BatchResponse> getBatchesByProduct(String productId) {

        List<Batch> batches = repository.findByProductId(productId);

        return batches.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BatchResponse reduceStock(Long id, ReduceStockRequest request) {

        Batch batch = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Batch not found"));
        if (request.getSoldQuantity() <= 0) {
            throw new RuntimeException(
                    "Sold quantity must be greater than zero"
            );
        }

        if (batch.getExpiryDate().isBefore(LocalDate.now())) {
            batch.setStatus("EXPIRED");
            repository.save(batch);
            throw new RuntimeException("Batch is expired");
        }

        // Status validation
        switch (batch.getStatus()) {

            case "SPOILED":
                throw new RuntimeException("Cannot sell spoiled batch");

            case "RECALLED":
                throw new RuntimeException("Cannot sell recalled batch");

            case "EXPIRED":
                throw new RuntimeException("Batch is expired");

            case "ACTIVE":
                break;

            default:
                throw new RuntimeException("Invalid batch status");
        }

        // Stock validation
        if (batch.getRemainingQuantity() <= 0) {
            throw new RuntimeException("No stock available in this batch");
        }

        if (batch.getRemainingQuantity() < request.getSoldQuantity()) {
            throw new RuntimeException("Not enough stock available");
        }

        int newQty = batch.getRemainingQuantity() - request.getSoldQuantity();

        batch.setRemainingQuantity(newQty);

        // ✅ mark batch as out of stock
        if (newQty == 0) {

            batch.setStatus("OUT_OF_STOCK");
        }

        repository.save(batch);

        int totalStock = calculateTotalStock(batch.getProductId());
        productClient.updateStock(
                batch.getProductId(),
                totalStock
        );

        if (newQty <= 20) { // threshold

            BatchEvent event = new BatchEvent();
            event.setEventType("STOCK_REDUCED");
            ProductResponse product =
                    productClient.getProduct(
                            batch.getProductId()
                    );
            event.setProductName(product.getName());
            event.setBatchId(batch.getId());
            event.setExpiryDate(batch.getExpiryDate());
            event.setRemainingQuantity(newQty);

            producer.sendEvent(event);

            dashboardEventProducer.sendLowStockAlert(
                    product.getName()
            );

            System.out.println("Stock reduced alert sent for batch: " + batch.getId());
        }

        return mapToResponse(batch);
    }

    @Override
    public List<BatchResponse> getExpiringSoon(int days) {
        LocalDate today = LocalDate.now();
        LocalDate future = today.plusDays(days);
        List<Batch> batches =
                repository.findByExpiryDateBetweenAndStatus(
                        today,
                        future,
                        "ACTIVE"
                );

        return batches.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<BatchResponse> getAvailableBatches(String productId) {
        List<Batch> batches =
                repository
                        .findByProductIdAndStatusAndRemainingQuantityGreaterThanOrderByExpiryDateAsc(
                                productId,
                                "ACTIVE",
                                0
                        );

        return batches.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BatchResponse spoilBatch(Long id) {
        Batch batch = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Batch not found"));
        batch.setStatus("SPOILED");
        batch.setRemainingQuantity(0);
        repository.save(batch);
        int totalStock = calculateTotalStock(batch.getProductId());
        productClient.updateStock(
                batch.getProductId(),
                totalStock
        );
        return mapToResponse(batch);
    }

    @Override
    public BatchResponse recallBatch(Long id) {
        Batch batch = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Batch not found"));
        batch.setStatus("RECALLED");
        batch.setRemainingQuantity(0);
        repository.save(batch);
        int totalStock = calculateTotalStock(batch.getProductId());
        productClient.updateStock(
                batch.getProductId(),
                totalStock
        );
        return mapToResponse(batch);
    }

    @Override
    public Long getBatchCount() {
        return repository.count();
    }


    @Override
    public Long getExpiringSoonCount() {
        LocalDate today = LocalDate.now();
        LocalDate future = today.plusDays(7);
        return (long) repository
                .findByExpiryDateBetweenAndStatus(
                        today,
                        future,
                        "ACTIVE"
                )
                .size();
    }

    @Override
    public List<BatchResponse> getAllBatches() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private BatchResponse mapToResponse(Batch batch) {

        return BatchResponse.builder()
                .id(batch.getId())
                .batchNumber(batch.getBatchNumber())
                .productId(batch.getProductId())
                .productName(batch.getProductName())
                .supplierId(batch.getSupplierId())
                .locationId(batch.getLocationId())
                .quantity(batch.getQuantity())
                .remainingQuantity(batch.getRemainingQuantity())
                .manufactureDate(batch.getManufactureDate())
                .expiryDate(batch.getExpiryDate())
                .purchasePrice(batch.getPurchasePrice())
                .status(batch.getStatus())
                .build();
    }

    private int calculateTotalStock(String productId){
        return repository
                .findByProductIdAndStatus(productId, "ACTIVE")
                .stream()
                .mapToInt(Batch::getRemainingQuantity)
                .sum();
    }


    @Transactional
    public List<BatchAllocationResponse> allocateBatch(AllocateBatchRequest request) {
        if (request.getQuantity() <= 0) {

            throw new RuntimeException(
                    "Quantity must be greater than zero"
            );
        }

        int requiredQty = request.getQuantity();

        List<Batch> batches =
                repository
                        .findByProductIdAndStatusAndRemainingQuantityGreaterThanOrderByExpiryDateAsc(
                                request.getProductId(),
                                "ACTIVE",
                                0
                        );

        List<BatchAllocationResponse> allocations = new ArrayList<>();

        for (Batch batch : batches) {

            if (requiredQty <= 0) {
                break;
            }

            int available = batch.getRemainingQuantity();

            int allocated = Math.min(available, requiredQty);

            batch.setRemainingQuantity(available - allocated);

            requiredQty -= allocated;

            allocations.add(
                    BatchAllocationResponse.builder()
                            .batchId(batch.getId())
                            .batchNumber(batch.getBatchNumber())
                            .allocatedQuantity(allocated)
                            .build()
            );

            repository.save(batch);
        }

        if (requiredQty > 0) {
            throw new RuntimeException("Not enough stock available");
        }


        int totalStock = calculateTotalStock(
                request.getProductId()
        );

        productClient.updateStock(
                request.getProductId(),
                totalStock
        );
        return allocations;
    }

    public BatchResponse getPreferredSupplier(
            String productId
    ){
        Batch batch =
                repository
                        .findFirstByProductIdAndStatusOrderByExpiryDateDesc(
                                productId,
                                "ACTIVE"
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "No supplier found"
                                )
                        );

        return mapToResponse(batch);
    }

    @Override
    public AvaliableStockResponse getAvailableStock(String productId) {

        List<Batch> batches =
                repository.findByProductIdAndStatus(
                        productId,
                        "ACTIVE"
                );

        int availableStock =
                batches.stream()
                        .mapToInt(Batch::getRemainingQuantity)
                        .sum();

        return AvaliableStockResponse.builder()
                .productId(productId)
                .availableStock(availableStock)
                .build();
    }

}
