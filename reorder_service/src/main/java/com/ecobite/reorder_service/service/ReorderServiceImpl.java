package com.ecobite.reorder_service.service;

import com.ecobite.reorder_service.DTOs.event.ReorderEvent;
import com.ecobite.reorder_service.DTOs.request.ReorderRequest;
import com.ecobite.reorder_service.DTOs.response.ProductResponse;
import com.ecobite.reorder_service.DTOs.response.ReorderResponse;
import com.ecobite.reorder_service.DTOs.response.SupplierResponse;
import com.ecobite.reorder_service.entity.Reorder;
import com.ecobite.reorder_service.exception.BadRequestException;
import com.ecobite.reorder_service.exception.ResourceNotFoundException;
import com.ecobite.reorder_service.feign.BatchClient;
import com.ecobite.reorder_service.feign.ProductClient;
import com.ecobite.reorder_service.feign.SupplierClient;
import com.ecobite.reorder_service.kafka.ReorderProducer;
import com.ecobite.reorder_service.repository.ReorderRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReorderServiceImpl implements ReorderService {

    private final ReorderRepository repository;
    private final ProductClient productClient;
    private final SupplierClient supplierClient;
    private final BatchClient batchClient;
    private final ReorderProducer reorderProducer;

    public ReorderServiceImpl(ReorderRepository repository, ProductClient productClient, SupplierClient supplierClient, BatchClient batchClient, ReorderProducer reorderProducer) {
        this.repository = repository;
        this.productClient = productClient;
        this.supplierClient = supplierClient;
        this.batchClient = batchClient;
        this.reorderProducer = reorderProducer;
    }

    @Override
    public ReorderResponse createReorder(ReorderRequest request) {

        // 🔹 1. Validate input
        if (request.getProductId() == null || request.getQuantity() <= 0) {
            throw new BadRequestException("Invalid request product count");
        }

        // 🔹 2. Get product from product-service
        ProductResponse product = null;

        try {

            product = productClient.getProduct(request.getProductId());

        } catch (FeignException.NotFound ex) {

            throw new ResourceNotFoundException(
                    "Product with ID " + request.getProductId() + " not found"
            );

        }

        // 🔹 3. Check reorder condition
        if (product.getStock() > product.getReorderLevel()) {
            throw new BadRequestException("Stock is sufficient. No need to reorder.");
        }

        // 🔹 4. Get batches
        var batches = batchClient.getBatchByProductId(product.getId());

        if (batches == null || batches.isEmpty()) {
            throw new BadRequestException("No batches found for this product");
        }

        //Filter valid batch
        var batch = batches.stream()
                .filter(b -> "ACTIVE".equalsIgnoreCase(b.getStatus()))
                .filter(b -> b.getRemainingQuantity() > 0)
                .findFirst()
                .orElseThrow(() -> new BadRequestException("No valid batch available"));

        //Get supplier

        // 🔹 6. Get best supplier
        SupplierResponse supplier;

        try {

            supplier = supplierClient.getBestSupplier(
                    product.getId()
            );

        } catch (FeignException.NotFound ex) {

            throw new ResourceNotFoundException(
                    "Best supplier not found for product: "
                            + product.getId()
            );

        } catch (FeignException ex) {

            throw new RuntimeException(
                    "Supplier service unavailable"
            );
        }

        // 7. Save reorder
        Reorder reorder = Reorder.builder()
                .productId(product.getId())
                .supplierId(supplier.getId())
                .quantity(request.getQuantity())
                .status("CREATED")
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(reorder);

        // SEND EVENT
        ReorderEvent event = new ReorderEvent(
                product.getId(),
                supplier.getId(),
                request.getQuantity(),
                "Reorder created for product " + product.getId()
        );

        reorderProducer.sendEvent(event);

        // 🔹 8. Return response
        return ReorderResponse.builder()
                .id(reorder.getId())
                .productId(reorder.getProductId())
                .supplierId(reorder.getSupplierId())
                .quantity(reorder.getQuantity())
                .status(reorder.getStatus())
                .createdAt(reorder.getCreatedAt())
                .build();
    }

    @Override
    public Long getPendingReordersCount() {
        return repository.countByStatus("CREATED");
    }
}
