package com.ecobite.location_service.controller;

import com.ecobite.location_service.DTO.*;
import com.ecobite.location_service.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // ================= WRITE OPERATIONS =================

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER') or hasAuthority('LOCATION_CREATE')"
    )
    @PostMapping
    public ResponseEntity<LocationResponse> create(
            @Valid @RequestBody CreateLocationRequest request) {

        return ResponseEntity.ok(
                service.createLocation(request)
        );
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER') or hasAuthority('LOCATION_ASSIGN_BATCH')"
    )
    @PostMapping("/assign")
    public ResponseEntity<Map<String, String>> assign(
            @Valid @RequestBody AssignBatchRequest request) {

        service.assignBatch(request);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Batch assigned successfully"
                )
        );
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER') or hasAuthority('LOCATION_MOVE_BATCH')"
    )
    @PostMapping("/move")
    public ResponseEntity<Map<String, Object>> move(
            @Valid @RequestBody MoveBatchRequest request) {

        service.moveBatch(request);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Batch moved successfully",
                        "batchId", request.getBatchId(),
                        "fromLocationId", request.getFromLocationId(),
                        "toLocationId", request.getToLocationId(),
                        "quantity", request.getQuantity()
                )
        );
    }

    // ================= READ OPERATIONS =================

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','STAFF') or hasAuthority('LOCATION_READ')"
    )
    @GetMapping
    public ResponseEntity<List<LocationResponse>> getAllLocations() {
        return ResponseEntity.ok(
                service.getAllLocations()
        );
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','STAFF') or hasAuthority('LOCATION_READ')"
    )
    @GetMapping("/warehouses/count")
    public Long getWarehouseCount() {
        return service.getWarehouseCount();
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','STAFF') or hasAuthority('LOCATION_READ')"
    )
    @GetMapping("/{locationId}/inventory")
    public ResponseEntity<List<InventoryLocationResponse>>
    getInventoryByLocation(
            @PathVariable Long locationId) {

        return ResponseEntity.ok(
                service.getInventoryByLocation(locationId)
        );
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','STAFF') or hasAuthority('LOCATION_READ')"
    )
    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<InventoryLocationResponse>>
    getLocationsByBatch(
            @PathVariable Long batchId) {

        return ResponseEntity.ok(
                service.getLocationsByBatch(batchId)
        );
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','STAFF') or hasAuthority('LOCATION_READ')"
    )
    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> getLocationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getLocationById(id)
        );
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER') or hasAuthority('LOCATION_UPDATE')"
    )
    @PutMapping("/{id}")
    public ResponseEntity<LocationResponse> updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody UpdateLocationRequest request) {

        return ResponseEntity.ok(
                service.updateLocation(id, request)
        );
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER') or hasAuthority('LOCATION_UPDATE')"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLocation(
            @PathVariable Long id) {

        service.deleteLocation(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Location deactivated successfully"
                )
        );
    }
}
