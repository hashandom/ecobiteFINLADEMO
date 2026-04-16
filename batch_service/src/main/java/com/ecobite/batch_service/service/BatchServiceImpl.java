package com.ecobite.batch_service.service;


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

    @Override
    public BatchResponse createBatch(CreateBatchRequest request) {

        ProductResponse product =
                productClient.getProduct(request.getProductId());

        if (product == null) {
            throw new ResourceNotFoundException("Product not found");
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

        if (batch.getRemainingQuantity() < request.getSoldQuantity()) {
            throw new RuntimeException("Not enough stock available");
        }

        batch.setRemainingQuantity(
                batch.getRemainingQuantity() - request.getSoldQuantity()
        );

        repository.save(batch);

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
