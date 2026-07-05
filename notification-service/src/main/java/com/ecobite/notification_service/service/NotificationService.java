package com.ecobite.notification_service.service;


import com.ecobite.notification_service.dto.event.BatchEvent;
import com.ecobite.notification_service.dto.event.ProductEvent;
import com.ecobite.notification_service.dto.event.ReorderEvent;
import com.ecobite.notification_service.dto.event.SupplierEvent;
import com.ecobite.notification_service.entity.Notification;

import java.util.List;

public interface NotificationService  {
    void handleBatchEvent(BatchEvent event);

    void handleProductEvent(ProductEvent event);

    void handleSupplierEvent(SupplierEvent event);

    void handleReorderEvent(ReorderEvent event);

    List<Notification> getNotificationsByRole(
            String role);

    Long getUnreadCount(
            String role);

    Long getTotalCount(
            String role);

    void markAsRead(
            Long notificationId);


}
