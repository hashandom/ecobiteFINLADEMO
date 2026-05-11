package com.ecobite.dashboard_service.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("dashboard_analytics")
public class DashboardAnalyticsCache {
    @Id
    private String id;
    private Double monthlySales;
    private Long monthlyOrders;
    private Long notifications;
}
