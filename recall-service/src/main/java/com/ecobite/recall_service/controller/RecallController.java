package com.ecobite.recall_service.controller;

import com.ecobite.recall_service.entity.Recall;
import com.ecobite.recall_service.service.RecallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recall")

public class RecallController {
    @Autowired
    private RecallService recallService;

    @PostMapping("/create")
    public Recall createRecall(@RequestBody Recall recall) {
        return recallService.createRecall(recall);
    }

    @GetMapping("/{batchId}")
    public Recall getRecall(@PathVariable Long batchId) {
        return recallService.getRecallByBatch(batchId);
    }
}
