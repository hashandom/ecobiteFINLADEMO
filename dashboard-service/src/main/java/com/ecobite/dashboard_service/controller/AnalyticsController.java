package com.ecobite.dashboard_service.controller;

import com.ecobite.dashboard_service.dto.response.AnalyticsResponse;
import com.ecobite.dashboard_service.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    @GetMapping("/monthly")
    public AnalyticsResponse getMonthlyAnalytics() {
        return analyticsService.getMonthlyAnalytics();
    }

}
