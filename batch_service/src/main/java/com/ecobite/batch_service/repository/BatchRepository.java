package com.ecobite.batch_service.repository;

import com.ecobite.batch_service.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BatchRepository extends JpaRepository<Batch, Long> {
    List<Batch> findByExpiryDateBefore(LocalDate date);

    List<Batch> findAllByOrderByExpiryDateAsc();
}
