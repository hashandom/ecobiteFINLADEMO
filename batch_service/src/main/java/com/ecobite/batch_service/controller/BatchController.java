package com.ecobite.batch_service.controller;


import com.ecobite.batch_service.dto.BatchRequestDTO;
import com.ecobite.batch_service.dto.BatchResponseDTO;
import com.ecobite.batch_service.dto.request.AllocateBatchRequest;
import com.ecobite.batch_service.dto.request.CreateBatchRequest;
import com.ecobite.batch_service.dto.request.ReduceStockRequest;
import com.ecobite.batch_service.dto.response.BatchAllocationResponse;
import com.ecobite.batch_service.dto.response.BatchResponse;
import com.ecobite.batch_service.service.BatchService;
import com.ecobite.batch_service.service.BatchServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batches")
@RequiredArgsConstructor
public class BatchController {
    private final BatchServiceImpl service;

    @PostMapping("/allocate")
    public ResponseEntity<List<BatchAllocationResponse>> allocate(
            @RequestBody AllocateBatchRequest request){

        return ResponseEntity.ok(service.allocateBatch(request));
    }

    @PostMapping
    public ResponseEntity<BatchResponse> createBatch(
            @RequestBody CreateBatchRequest request){

        return ResponseEntity.ok(service.createBatch(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchResponse> getBatch(@PathVariable Long id){
        return ResponseEntity.ok(service.getBatchById(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<BatchResponse>> getBatches(
            @PathVariable String productId){

        return ResponseEntity.ok(service.getBatchesByProduct(productId));
    }

    @PutMapping("/{id}/reduce-stock")
    public ResponseEntity<BatchResponse> reduceStock(
            @PathVariable Long id,
            @RequestBody ReduceStockRequest request){

        return ResponseEntity.ok(service.reduceStock(id, request));
    }

    @PutMapping("/{id}/spoil")
    public ResponseEntity<BatchResponse> spoilBatch(@PathVariable Long id){

        return ResponseEntity.ok(service.spoilBatch(id));
    }

    @PutMapping("/{id}/recall")
    public ResponseEntity<BatchResponse> recallBatch(@PathVariable Long id){

        return ResponseEntity.ok(service.recallBatch(id));
    }
}
