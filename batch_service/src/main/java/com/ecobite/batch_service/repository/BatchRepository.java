package com.ecobite.batch_service.repository;

import com.ecobite.batch_service.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BatchRepository extends JpaRepository<Batch, Long> {
    //Get all batches for a product
    List<Batch> findByProductId(String productId);

    //FIFO allocation (ACTIVE batches only)
    List<Batch>
    findByProductIdAndStatusAndRemainingQuantityGreaterThanOrderByExpiryDateAsc(
            String productId,
            String status,
            int qty
    );

    //Get expiring ACTIVE batches
    List<Batch> findByExpiryDateBetweenAndStatus(
            LocalDate start,
            LocalDate end,
            String status
    );

    // Scheduler - expired ACTIVE batches
    List<Batch> findByExpiryDateBeforeAndStatus(
            LocalDate date,
            String status
    );

    // Duplicate batch validation
    boolean existsByBatchNumber(String batchNumber);

    //  Product stock recalculation
    List<Batch> findByProductIdAndStatus(
            String productId,
            String status
    );


    List<Batch>
    findByExpiryDateBetweenAndStatusAndExpiryAlertSentFalse(
            LocalDate start,
            LocalDate end,
            String status
    );


    //latest active batch for a product
    Optional<Batch> findFirstByProductIdAndStatusOrderByExpiryDateDesc(
            String productId,
            String status
    );
}
