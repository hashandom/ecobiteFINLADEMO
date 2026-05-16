package com.ecobite.batch_service.service;


import com.ecobite.batch_service.KafKaEventProducer.BatchEventProducer;
import com.ecobite.batch_service.dto.Kafkaevent.BatchEvent;
import com.ecobite.batch_service.dto.request.AllocateBatchRequest;
import com.ecobite.batch_service.dto.request.CreateBatchRequest;
import com.ecobite.batch_service.dto.request.ReduceStockRequest;
import com.ecobite.batch_service.dto.response.BatchAllocationResponse;
import com.ecobite.batch_service.dto.response.BatchResponse;
import com.ecobite.batch_service.dto.response.ProductResponse;
import com.ecobite.batch_service.entity.Batch;
import com.ecobite.batch_service.exception.ResourceNotFoundException;
import com.ecobite.batch_service.feign.ProductClient;
import com.ecobite.batch_service.repository.BatchRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService{
    private final BatchRepository repository;
    private final ProductClient productClient;
    private final BatchEventProducer producer;

    @Override
    public BatchResponse createBatch(CreateBatchRequest request) {
        if (request.getExpiryDate().isBefore(request.getManufactureDate())) {
            throw new RuntimeException(
                    "Expiry date cannot be before manufacture date"
            );
        }

        if (repository.existsByBatchNumber(request.getBatchNumber())) {
            throw new RuntimeException(
                    "Batch number already exists"
            );
        }

        ProductResponse product;

        try {

            product = productClient.getProduct(request.getProductId());

        } catch (feign.FeignException.NotFound ex) {

            throw new ResourceNotFoundException("Product not found");

        } catch (feign.FeignException ex) {

            throw new RuntimeException("Product service unavailable");
        }

        Batch batch = Batch.builder()
                .batchNumber(request.getBatchNumber())
                .productId(request.getProductId())
                .supplierId(request.getSupplierId())
                .quantity(request.getQuantity())
                .remainingQuantity(request.getQuantity())
                .manufactureDate(request.getManufactureDate())
                .expiryDate(request.getExpiryDate())
                .purchasePrice(request.getPurchasePrice())
                .status("ACTIVE")
                .build();

        repository.save(batch);

        productClient.addStock(
                batch.getProductId(),
                batch.getQuantity()
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

        repository.save(batch);

        // DEDUCT PRODUCT STOCK
            productClient.deductStock(
                batch.getProductId(),
                request.getSoldQuantity()
        );

        if (newQty <= 20) { // threshold

            BatchEvent event = new BatchEvent();
            event.setEventType("STOCK_REDUCED");
            event.setProductName(batch.getProductId()); // change if you have productName
            event.setBatchId(batch.getId());
            event.setExpiryDate(batch.getExpiryDate());
            event.setRemainingQuantity(newQty);

            producer.sendEvent(event);

            System.out.println("Stock reduced alert sent for batch: " + batch.getId());
        }

        return mapToResponse(batch);
    }

    @Override
    public List<BatchResponse> getExpiringSoon(int days) {

        LocalDate today = LocalDate.now();
        LocalDate future = today.plusDays(days);

        List<Batch> batches =
                repository.findByExpiryDateBetween(today, future);

        return batches.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<BatchResponse> getAvailableBatches(String productId) {

        List<Batch> batches =
                repository
                        .findByProductIdAndRemainingQuantityGreaterThanOrderByExpiryDateAsc(
                                productId, 0);

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

        repository.save(batch);

        return mapToResponse(batch);
    }

    @Override
    public BatchResponse recallBatch(Long id) {

        Batch batch = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Batch not found"));

        batch.setStatus("RECALLED");

        repository.save(batch);

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
                .findByExpiryDateBetween(today, future)
                .size();
    }

    private BatchResponse mapToResponse(Batch batch) {

        return BatchResponse.builder()
                .id(batch.getId())
                .batchNumber(batch.getBatchNumber())
                .productId(batch.getProductId())
                .supplierId(batch.getSupplierId())
                .quantity(batch.getQuantity())
                .remainingQuantity(batch.getRemainingQuantity())
                .manufactureDate(batch.getManufactureDate())
                .expiryDate(batch.getExpiryDate())
                .purchasePrice(batch.getPurchasePrice())
                .status(batch.getStatus())
                .build();
    }

    @Transactional
    public List<BatchAllocationResponse> allocateBatch(AllocateBatchRequest request){

        int requiredQty = request.getQuantity();

        List<Batch> batches =
                repository.findByProductIdAndRemainingQuantityGreaterThanOrderByExpiryDateAsc(
                        request.getProductId(),0);

        List<BatchAllocationResponse> allocations = new ArrayList<>();

        for(Batch batch : batches){

            if(requiredQty <= 0){
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

        if(requiredQty > 0){
            throw new RuntimeException("Not enough stock available");
        }

        return allocations;
    }


}
