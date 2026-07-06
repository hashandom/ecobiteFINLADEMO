package com.ecobite.batch_service.controller;


import com.ecobite.batch_service.dto.request.AllocateBatchRequest;
import com.ecobite.batch_service.dto.request.CreateBatchRequest;
import com.ecobite.batch_service.dto.request.ReduceStockRequest;
import com.ecobite.batch_service.dto.response.BatchAllocationResponse;
import com.ecobite.batch_service.dto.response.BatchResponse;
import com.ecobite.batch_service.service.BatchServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batches")
@RequiredArgsConstructor
public class BatchController {
    private final BatchServiceImpl service;

    // ================= READ OPERATIONS =================

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','STAFF') or hasAuthority('BATCH_READ')"
    )
    @GetMapping
    public ResponseEntity<List<BatchResponse>> getAllBatches() {
        return ResponseEntity.ok(service.getAllBatches());
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','STAFF') or hasAuthority('BATCH_READ')"
    )
    @GetMapping("/expiring-soon")
    public ResponseEntity<List<BatchResponse>> getExpiringSoon(
            @RequestParam int days) {

        return ResponseEntity.ok(service.getExpiringSoon(days));
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','STAFF') or hasAuthority('BATCH_READ')"
    )
    @GetMapping("/count")
    public Long getBatchCount() {
        return service.getBatchCount();
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','STAFF') or hasAuthority('BATCH_READ')"
    )
    @GetMapping("/expiring/count")
    public Long getExpiringSoonCount() {
        return service.getExpiringSoonCount();
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','STAFF') or hasAuthority('BATCH_READ')"
    )
    @GetMapping("/{id}")
    public ResponseEntity<BatchResponse> getBatch(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getBatchById(id)
        );
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','STAFF') or hasAuthority('BATCH_READ')"
    )
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<BatchResponse>> getBatches(
            @PathVariable String productId) {

        return ResponseEntity.ok(
                service.getBatchesByProduct(productId)
        );
    }

    // ================= WRITE OPERATIONS =================

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER') or hasAuthority('BATCH_CREATE')"
    )
    @PostMapping
    public ResponseEntity<BatchResponse> createBatch(
            @Valid @RequestBody CreateBatchRequest request) {

        return ResponseEntity.ok(
                service.createBatch(request)
        );
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER') or hasAuthority('BATCH_ALLOCATE')"
    )
    @PostMapping("/allocate")
    public ResponseEntity<List<BatchAllocationResponse>> allocate(
            @RequestBody AllocateBatchRequest request) {

        return ResponseEntity.ok(
                service.allocateBatch(request)
        );
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER') or hasAuthority('BATCH_UPDATE')"
    )
    @PostMapping("/reduce/{batchId}")
    public String reduceStock(
            @PathVariable Long batchId,
            @RequestBody ReduceStockRequest request) {

        service.reduceStock(batchId, request);

        return "Stock updated successfully";
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER') or hasAuthority('BATCH_SPOIL')"
    )
    @PutMapping("/{id}/spoil")
    public ResponseEntity<BatchResponse> spoilBatch(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.spoilBatch(id)
        );
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER') or hasAuthority('BATCH_RECALL')"
    )
    @PutMapping("/{id}/recall")
    public ResponseEntity<BatchResponse> recallBatch(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.recallBatch(id)
        );
    }

    @GetMapping("/supplier/{productId}")
    public ResponseEntity<BatchResponse>
    getSupplierForProduct(
            @PathVariable String productId
    ){
        return ResponseEntity.ok(
                service.getPreferredSupplier(
                        productId
                )
        );
    }
}
