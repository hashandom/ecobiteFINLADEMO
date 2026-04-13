package com.ecobite.location_service.service;

import com.ecobite.location_service.entity.BatchLocation;

public interface LocationService {
    BatchLocation assignLocation(BatchLocation location);

    BatchLocation getLocationByBatch(Long batchId);

}
