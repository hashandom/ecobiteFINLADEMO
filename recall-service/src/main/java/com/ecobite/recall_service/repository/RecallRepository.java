package com.ecobite.recall_service.repository;

import com.ecobite.recall_service.entity.Recall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecallRepository extends JpaRepository<Recall, Long> {
    Optional<Recall> findByBatchId(Long batchId);
}
