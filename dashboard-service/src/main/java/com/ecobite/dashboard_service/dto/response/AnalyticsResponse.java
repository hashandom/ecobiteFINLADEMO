package com.ecobite.dashboard_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse implements Serializable  {

    private Double monthlySales;

    private Long monthlyOrders;

    private Long monthlyNotifications;

    private Long lowStockProducts;

    private Long expiredBatches;
}
