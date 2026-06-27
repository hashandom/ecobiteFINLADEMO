package com.ecobite.reorder_service.controller;

import com.ecobite.reorder_service.DTOs.request.ReorderRequest;
import com.ecobite.reorder_service.DTOs.response.ReorderResponse;
import com.ecobite.reorder_service.service.ReorderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reorders")
public class ReorderController {
    private final ReorderService service;

    public ReorderController(ReorderService service) {
        this.service = service;
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
}
