package com.ecobite.batch_service.repository;

import com.ecobite.batch_service.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BatchRepository extends JpaRepository<Batch, Long> {
    List<Batch> findByProductId(String productId);

    List<Batch> findByProductIdAndRemainingQuantityGreaterThanOrderByExpiryDateAsc(String productId, int qty);

    List<Batch> findByExpiryDateBetween(LocalDate start, LocalDate end);

    List<Batch> findByExpiryDateBeforeAndStatus(
            LocalDate date,
            String status
    );

    boolean existsByBatchNumber(String batchNumber);
}
