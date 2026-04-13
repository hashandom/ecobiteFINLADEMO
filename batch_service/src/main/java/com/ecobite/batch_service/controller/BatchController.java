package com.ecobite.batch_service.controller;


import com.ecobite.batch_service.dto.BatchRequestDTO;
import com.ecobite.batch_service.dto.BatchResponseDTO;
import com.ecobite.batch_service.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batches")
public class BatchController {
    @Autowired
    private BatchService batchService;

    @PostMapping
    public BatchResponseDTO createBatch(@RequestBody BatchRequestDTO request) {
        return batchService.createBatch(request);
    }

    @GetMapping("/expiring-soon")
    public List<BatchResponseDTO> getExpiringSoon() {
        return batchService.getExpiringSoon();
    }

    @GetMapping("/fefo")
    public List<BatchResponseDTO> getFefoBatches() {
        return batchService.getFefoBatches();
    }
}
