package com.ecobite.location_service.repository;

import com.ecobite.location_service.entity.BatchLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<BatchLocation, Long> {

    Optional<BatchLocation> findByLocationCode(String code);

    boolean existsByLocationCode(String locationCode);

    @Query("SELECT COUNT(DISTINCT l.warehouse) FROM BatchLocation l")
    Long countDistinctWarehouses();
}
