package com.ecobite.location_service.service;

import com.ecobite.location_service.entity.BatchLocation;
import com.ecobite.location_service.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository repository;

    @Override
    public BatchLocation assignLocation(BatchLocation location) {
        return repository.save(location);
    }

    @Override
    public BatchLocation getLocationByBatch(Long batchId) {

        return repository.findByBatchId(batchId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

    }
}
