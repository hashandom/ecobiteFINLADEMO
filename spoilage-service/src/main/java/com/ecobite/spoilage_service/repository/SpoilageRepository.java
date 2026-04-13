package com.ecobite.spoilage_service.repository;

import com.ecobite.spoilage_service.entity.SpoilageLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpoilageRepository extends JpaRepository<SpoilageLog, Long> {
}
