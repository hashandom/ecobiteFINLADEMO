package com.ecobite.dashboard_service.service;

import com.ecobite.dashboard_service.dto.response.AnalyticsResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceImpl implements AnalyticsService{
    @Override
    @Cacheable(value = "monthlyAnalytics")
    public AnalyticsResponse getMonthlyAnalytics() {

        return AnalyticsResponse.builder()
                .monthlySales(50000.0)
                .monthlyOrders(100L)
                .monthlyNotifications(20L)
                .build();
    }
}
