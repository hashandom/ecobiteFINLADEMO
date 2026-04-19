package com.ecobite.notification_service.service;


import com.ecobite.notification_service.dto.event.BatchEvent;
import com.ecobite.notification_service.dto.event.ProductEvent;
import com.ecobite.notification_service.entity.Notification;

import java.util.List;

public interface NotificationService  {
    void handleBatchEvent(BatchEvent event);

    void handleProductEvent(ProductEvent event);
}
