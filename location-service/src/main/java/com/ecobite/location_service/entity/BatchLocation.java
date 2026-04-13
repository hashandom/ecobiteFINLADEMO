package com.ecobite.location_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BatchLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long batchId;

    private String storageType;

    private String locationName;

    public Long getId() { return id; }

    public Long getBatchId() { return batchId; }

    public String getStorageType() { return storageType; }

    public String getLocationName() { return locationName; }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
