package com.ecobite.reorder_service.controller;

import com.ecobite.reorder_service.DTOs.request.ReorderRequest;
import com.ecobite.reorder_service.DTOs.response.ReorderResponse;
import com.ecobite.reorder_service.service.ReorderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reorders")
public class ReorderController {
    private final ReorderService service;

    public ReorderController(ReorderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReorderResponse> create(@RequestBody ReorderRequest request) {
        return ResponseEntity.ok(service.createReorder(request));
    }
}
