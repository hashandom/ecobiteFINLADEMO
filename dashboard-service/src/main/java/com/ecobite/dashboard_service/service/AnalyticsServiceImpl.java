package com.ecobite.dashboard_service.service;

import com.ecobite.dashboard_service.client.BatchClient;
import com.ecobite.dashboard_service.client.NotificationClient;
import com.ecobite.dashboard_service.client.ProductClient;
import com.ecobite.dashboard_service.client.ReorderClient;
import com.ecobite.dashboard_service.dto.response.AnalyticsResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceImpl implements AnalyticsService{
    private final ReorderClient reorderClient;
    private final NotificationClient notificationClient;
    private final ProductClient productClient;
    private final BatchClient batchClient;

    public AnalyticsServiceImpl(ReorderClient reorderClient, NotificationClient notificationClient, ProductClient productClient, BatchClient batchClient) {
        this.reorderClient = reorderClient;
        this.notificationClient = notificationClient;
        this.productClient = productClient;
        this.batchClient = batchClient;
    }

    @Override
    @Cacheable(value = "monthlyAnalytics")
    public AnalyticsResponse getMonthlyAnalytics() {

        Long orders =
                reorderClient.getPendingReorders();

        Long notifications =
                notificationClient.getTotalNotifications();

        Long lowStock =
                productClient.getLowStockCount();

        Long expired =
                batchClient.getBatchCount();

        return AnalyticsResponse.builder()
                .monthlySales(50000.0)
                .monthlyOrders(orders)
                .monthlyNotifications(notifications)
                .lowStockProducts(lowStock)
                .expiredBatches(expired)
                .build();
    }
    }

