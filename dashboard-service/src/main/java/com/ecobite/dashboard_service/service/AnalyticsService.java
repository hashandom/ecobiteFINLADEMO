package com.ecobite.dashboard_service.service;

import com.ecobite.dashboard_service.dto.response.AnalyticsResponse;

public interface AnalyticsService {
    AnalyticsResponse getMonthlyAnalytics();
}
