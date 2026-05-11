package com.ecobite.dashboard_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardOverviewResponse {

    private InventorySummary inventory;
    private BatchSummary batch;
    private SupplierSummary supplier;
    private ReorderSummary reorder;
    private NotificationSummary notification;
    private LocationSummary location;
}
