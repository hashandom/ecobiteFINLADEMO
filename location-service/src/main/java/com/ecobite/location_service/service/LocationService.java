package com.ecobite.location_service.service;

import com.ecobite.location_service.DTO.*;
import com.ecobite.location_service.entity.BatchLocation;

import java.util.List;

public interface LocationService {
    LocationResponse createLocation(CreateLocationRequest request);

    void assignBatch(AssignBatchRequest request);

    void moveBatch(MoveBatchRequest request);

    List<LocationResponse> getAllLocations();

    List<InventoryLocationResponse> getInventoryByLocation(Long locationId);


    List<InventoryLocationResponse> getLocationsByBatch(Long batchId);

    Long getWarehouseCount();
}
