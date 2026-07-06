package com.ecobite.reorder_service.controller;

import com.ecobite.reorder_service.DTOs.request.ReorderRequest;
import com.ecobite.reorder_service.DTOs.response.LowStockSupplierResponse;
import com.ecobite.reorder_service.DTOs.response.ReorderResponse;
import com.ecobite.reorder_service.service.ReorderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reorders")
public class ReorderController {
    private final ReorderService service;
    private final ReorderService reorderService;

    public ReorderController(ReorderService service, ReorderService reorderService) {
        this.service = service;
        this.reorderService = reorderService;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ReorderResponse> create(@RequestBody ReorderRequest request) {
        return ResponseEntity.ok(service.createReorder(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    @GetMapping("/pending/count")
    public Long getPendingReordersCount() {
        return service.getPendingReordersCount();
    }

    @GetMapping("/low-stock-suppliers")
    public ResponseEntity<
            List<LowStockSupplierResponse>>
    getLowStockSuppliers(){
        return ResponseEntity.ok(
                reorderService
                        .getLowStockWithSuppliers()
        );
    }
}
