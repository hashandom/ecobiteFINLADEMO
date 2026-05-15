package com.ecobite.reorder_service.service;

import com.ecobite.reorder_service.DTOs.event.ReorderEvent;
import com.ecobite.reorder_service.DTOs.request.ReorderRequest;
import com.ecobite.reorder_service.DTOs.response.ReorderResponse;
import com.ecobite.reorder_service.entity.Reorder;
import com.ecobite.reorder_service.exception.BadRequestException;
import com.ecobite.reorder_service.feign.BatchClient;
import com.ecobite.reorder_service.feign.ProductClient;
import com.ecobite.reorder_service.feign.SupplierClient;
import com.ecobite.reorder_service.kafka.ReorderProducer;
import com.ecobite.reorder_service.repository.ReorderRepository;
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
            throw new BadRequestException("Invalid request data");
        }

        // 🔹 2. Get product from product-service
        var product = productClient.getProduct(request.getProductId());

        if (product == null) {
            throw new BadRequestException("Product not found");
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
        var supplier = supplierClient.getSupplier(batch.getSupplierId());

        if (supplier == null) {
            throw new BadRequestException("Supplier not found");
        }

        // 7. Save reorder
        Reorder reorder = Reorder.builder()
                .productId(product.getId())
                .supplierId(batch.getSupplierId())
                .quantity(request.getQuantity())
                .status("CREATED")
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(reorder);

        // SEND EVENT
        ReorderEvent event = new ReorderEvent(
                product.getId(),
                batch.getSupplierId(),
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
        return repository.countByStatus("PENDING");
    }
}
