package com.ecobite.location_service.repository;

import com.ecobite.location_service.entity.InventoryLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryLocationRepository extends JpaRepository<InventoryLocation,Long> {
    List<InventoryLocation> findByLocationId(Long locationId);
    List<InventoryLocation> findByBatchId(Long batchId);
    Optional<InventoryLocation> findByBatchIdAndLocationId(
            Long batchId,
            Long locationId
    );
}
