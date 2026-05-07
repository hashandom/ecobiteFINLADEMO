package com.ecobite.spoilage_service.repository;

import com.ecobite.spoilage_service.entity.Spoilage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpoilageRepository extends JpaRepository<Spoilage,Long> {
}
