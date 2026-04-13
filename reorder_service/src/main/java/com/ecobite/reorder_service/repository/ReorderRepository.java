package com.ecobite.reorder_service.repository;

import com.ecobite.reorder_service.entity.Reorder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReorderRepository extends JpaRepository<Reorder, Long> {
}
