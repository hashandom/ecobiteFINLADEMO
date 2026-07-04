package com.ecobite.location_service.repository;

import com.ecobite.location_service.entity.BatchLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<BatchLocation, Long> {

    Optional<BatchLocation> findByLocationCode(String code);

    boolean existsByLocationCode(String locationCode);

    List<BatchLocation> findByIsActiveTrue();

    Optional<BatchLocation> findByIdAndIsActiveTrue(Long id);

    @Query("""
    SELECT COUNT(DISTINCT l.warehouse)
    FROM BatchLocation l
    WHERE l.isActive = true
    """)
    Long countDistinctWarehouses();
}
