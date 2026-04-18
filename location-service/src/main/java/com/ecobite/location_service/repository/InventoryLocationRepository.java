package com.ecobite.location_service.repository;

import com.ecobite.location_service.entity.InventoryLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryLocationRepository extends JpaRepository<InventoryLocation,Long> {
    List<InventoryLocation> findByLocationId(Long locationId);
    List<InventoryLocation> findByBatchId(Long batchId);
}
