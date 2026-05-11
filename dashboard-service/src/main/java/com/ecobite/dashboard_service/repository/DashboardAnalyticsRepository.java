package com.ecobite.dashboard_service.repository;

import com.ecobite.dashboard_service.entity.DashboardAnalyticsCache;
import org.springframework.data.repository.CrudRepository;

public interface DashboardAnalyticsRepository extends CrudRepository<DashboardAnalyticsCache, String> {
}
