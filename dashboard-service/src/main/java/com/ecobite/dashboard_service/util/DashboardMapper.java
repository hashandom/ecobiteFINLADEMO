package com.ecobite.dashboard_service.util;

import com.ecobite.dashboard_service.dto.response.*;

public class DashboardMapper {
    private DashboardMapper() {
    }

    /**
     * Build Inventory Summary
     */
    public static InventorySummary mapInventorySummary(
            Long totalProducts,
            Long lowStockProducts
    ) {

        return InventorySummary.builder()
                .totalProducts(totalProducts)
                .lowStockProducts(lowStockProducts)
                .build();
    }

    /**
     * Build Batch Summary
     */
    public static BatchSummary mapBatchSummary(
            Long totalBatches,
            Long expiringSoon
    ) {

        return BatchSummary.builder()
                .totalBatches(totalBatches)
                .expiringSoon(expiringSoon)
                .build();
    }

    /**
     * Build Supplier Summary
     */
    public static SupplierSummary mapSupplierSummary(
            Long totalSuppliers
    ) {

        return SupplierSummary.builder()
                .totalSuppliers(totalSuppliers)
                .build();
    }

    /**
     * Build Reorder Summary
     */
    public static ReorderSummary mapReorderSummary(
            Long pendingReorders
    ) {

        return ReorderSummary.builder()
                .pendingReorders(pendingReorders)
                .build();
    }

    /**
     * Build Notification Summary
     */
    public static NotificationSummary mapNotificationSummary(
            Long unreadNotifications
    ) {

        return NotificationSummary.builder()
                .unreadNotifications(unreadNotifications)
                .build();
    }

    /**
     * Build Location Summary
     */
    public static LocationSummary mapLocationSummary(
            Long warehouseCount
    ) {

        return LocationSummary.builder()
                .warehouseCount(warehouseCount)
                .build();
    }

    /**
     * Build Dashboard Overview
     */
    public static DashboardOverviewResponse mapDashboardOverview(
            InventorySummary inventory,
            BatchSummary batch,
            SupplierSummary supplier,
            ReorderSummary reorder,
            NotificationSummary notification,
            LocationSummary location
    ) {

        return DashboardOverviewResponse.builder()
                .inventory(inventory)
                .batch(batch)
                .supplier(supplier)
                .reorder(reorder)
                .notification(notification)
                .location(location)
                .build();
    }
}
