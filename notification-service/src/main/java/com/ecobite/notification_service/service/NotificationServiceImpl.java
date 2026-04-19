package com.ecobite.notification_service.service;

import com.ecobite.notification_service.dto.event.BatchEvent;
import com.ecobite.notification_service.dto.event.ProductEvent;
import com.ecobite.notification_service.entity.Notification;
import com.ecobite.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    @Override
    public void handleBatchEvent(BatchEvent event) {
        String message = "Batch " + event.getBatchId() +
                " of " + event.getProductName() +
                " expires on " + event.getExpiryDate();
        saveNotification(message, "BATCH");
    }

    @Override
    public void handleProductEvent(ProductEvent event) {
        String message = "Low stock for " + event.getProductName() +
                " remaining: " + event.getRemainingStock();
        saveNotification(message, "PRODUCT");
    }


    private void saveNotification(String message, String type) {

        Notification notification = Notification.builder()
                .message(message)
                .type(type)
                .status("SENT")
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(notification);

        System.out.println("Notification Saved: " + message);
    }



}
