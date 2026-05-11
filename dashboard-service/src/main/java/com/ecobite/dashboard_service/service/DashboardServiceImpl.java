package com.ecobite.dashboard_service.service;

import com.ecobite.dashboard_service.client.*;
import com.ecobite.dashboard_service.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService{
    private final ProductClient productClient;
    private final BatchClient batchClient;
    private final SupplierClient supplierClient;
    private final ReorderClient reorderClient;
    private final NotificationClient notificationClient;
    private final LocationClient locationClient;

    @Override
    public DashboardOverviewResponse getDashboardOverview() {

        return DashboardOverviewResponse.builder()

                .inventory(
                        InventorySummary.builder()
                                .totalProducts(productClient.getProductCount())
                                .lowStockProducts(productClient.getLowStockCount())
                                .build()
                )

                .batch(
                        BatchSummary.builder()
                                .totalBatches(batchClient.getBatchCount())
                                .expiringSoon(batchClient.getExpiringSoonCount())
                                .build()
                )

                .supplier(
                        SupplierSummary.builder()
                                .totalSuppliers(supplierClient.getSupplierCount())
                                .build()
                )

                .reorder(
                        ReorderSummary.builder()
                                .pendingReorders(reorderClient.getPendingReorders())
                                .build()
                )

                .notification(
                        NotificationSummary.builder()
                                .unreadNotifications(notificationClient.getUnreadCount())
                                .build()
                )

                .location(
                        LocationSummary.builder()
                                .warehouseCount(locationClient.getWarehouseCount())
                                .build()
                )

                .build();
    }
}
