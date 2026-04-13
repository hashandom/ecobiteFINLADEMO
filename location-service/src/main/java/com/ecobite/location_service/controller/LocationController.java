package com.ecobite.location_service.controller;

import com.ecobite.location_service.entity.BatchLocation;
import com.ecobite.location_service.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @PostMapping("/assign")
    public BatchLocation assignLocation(@RequestBody BatchLocation location) {
        return locationService.assignLocation(location);
    }

    @GetMapping("/{batchId}")
    public BatchLocation getLocation(@PathVariable Long batchId) {
        return locationService.getLocationByBatch(batchId);
    }
}
