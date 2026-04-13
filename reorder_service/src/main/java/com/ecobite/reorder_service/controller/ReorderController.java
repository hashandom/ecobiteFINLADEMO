package com.ecobite.reorder_service.controller;

import com.ecobite.reorder_service.entity.Reorder;
import com.ecobite.reorder_service.service.ReorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reorders")
public class ReorderController {
    @Autowired
    private ReorderService reorderService;

    @PostMapping("/create")
    public Reorder createReorder(@RequestBody Reorder reorder) {
        return reorderService.createReorder(reorder);
    }

    @GetMapping
    public List<Reorder> getAllReorders() {
        return reorderService.getAllReorders();
    }
}
