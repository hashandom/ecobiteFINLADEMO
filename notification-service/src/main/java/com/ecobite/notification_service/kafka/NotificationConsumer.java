package com.ecobite.notification_service.kafka;

import com.ecobite.notification_service.entity.Notification;
import com.ecobite.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {
    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = "inventory-events", groupId = "notification-group")
    public void consumeEvent(String message) {

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setType("SYSTEM");

        notificationService.saveNotification(notification);
    }
}
