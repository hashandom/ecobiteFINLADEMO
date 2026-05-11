package com.ecobite.dashboard_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalyticsResponse {
    private Double monthlySales;
    private Long monthlyOrders;
    private Long monthlyNotifications;
}
