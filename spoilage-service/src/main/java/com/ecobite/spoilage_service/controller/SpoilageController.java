package com.ecobite.spoilage_service.controller;

import com.ecobite.spoilage_service.entity.SpoilageLog;
import com.ecobite.spoilage_service.service.SpoilageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spoilage")
public class SpoilageController {
    @Autowired
    private SpoilageService spoilageService;

    @PostMapping("/add")
    public SpoilageLog addSpoilage(@RequestBody SpoilageLog spoilage) {
        return spoilageService.addSpoilage(spoilage);
    }

    @GetMapping("/report")
    public List<SpoilageLog> getReport() {
        return spoilageService.getSpoilageReport();
    }
}
