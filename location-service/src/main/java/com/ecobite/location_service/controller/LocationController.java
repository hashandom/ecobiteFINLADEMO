package com.ecobite.location_service.controller;

import com.ecobite.location_service.DTO.*;
import com.ecobite.location_service.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/location")
public class LocationController {
    private final LocationService service;

    public LocationController(LocationService service) {
        this.service = service;
    }

    //Create Location
    @PostMapping
    public ResponseEntity<LocationResponse> create(
            @Valid @RequestBody CreateLocationRequest request) {

        return ResponseEntity.ok(service.createLocation(request));
    }

    //Assign Batch
    @PostMapping("/assign")
    public ResponseEntity<Map<String, String>> assign(
            @Valid @RequestBody AssignBatchRequest request) {

        service.assignBatch(request);

        return ResponseEntity.ok(Map.of(
                "message", "Batch assigned successfully"
        ));
    }

    // Move Batch
    @PostMapping("/move")
    public ResponseEntity<Map<String, Object>> move(
            @Valid @RequestBody MoveBatchRequest request) {

        service.moveBatch(request);

        return ResponseEntity.ok(Map.of(
                "message", "Batch moved successfully",
                "batchId", request.getBatchId(),
                "fromLocationId", request.getFromLocationId(),
                "toLocationId", request.getToLocationId(),
                "quantity", request.getQuantity()
        ));
    }

    //  Get All Locations
    @GetMapping
    public ResponseEntity<List<LocationResponse>> getAllLocations() {
        return ResponseEntity.ok(service.getAllLocations());
    }

    // Warehouse Count for Dashboard
    @GetMapping("/warehouses/count")
    public Long getWarehouseCount() {
        return service.getWarehouseCount();
    }

    //  Get Inventory by Location
    @GetMapping("/{locationId}/inventory")
    public ResponseEntity<List<InventoryLocationResponse>> getInventoryByLocation(
            @PathVariable Long locationId) {

        return ResponseEntity.ok(service.getInventoryByLocation(locationId));
    }

    //  Get Locations by Batch (for recall-service)
    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<InventoryLocationResponse>> getLocationsByBatch(
            @PathVariable Long batchId) {

        return ResponseEntity.ok(service.getLocationsByBatch(batchId));
    }
}
