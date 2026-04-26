package com.ecobite.notification_service.service;

import com.ecobite.notification_service.dto.event.BatchEvent;
import com.ecobite.notification_service.dto.event.ProductEvent;
import com.ecobite.notification_service.dto.event.SupplierEvent;
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
    private final NotificationRepository notificationRepository;

    @Override
    public void handleBatchEvent(BatchEvent event) {
        if ("BATCH_EXPIRING".equals(event.getEventType())) {
            String message = "Batch " + event.getBatchId() +
                    " of " + event.getProductName() +
                    " expires on " + event.getExpiryDate();
            saveNotification(message, "EXPIRY_ALERT");
        }
        else if ("BATCH_CREATED".equals(event.getEventType())) {
            String message = "New batch created: " + event.getBatchId() +
                    " for " + event.getProductName();
            saveNotification(message, "BATCH_CREATED");
        }
    }

    @Override
    public void handleProductEvent(ProductEvent event) {
        switch (event.getEventType()) {

            case "LOW_STOCK":
                String message = "⚠️ Low stock for " + event.getProductName() +
                        " remaining: " + event.getStock() +
                        " (Reorder Level: " + event.getReorderLevel() + ")";

                saveNotification(message, "STOCK_ALERT");
                break;

            default:
                System.out.println("Unknown product event: " + event.getEventType());
        }
    }

    @Override
    public void handleSupplierEvent(SupplierEvent event) {
        String message = "New Supplier Added: " + event.getSupplierName();
        saveNotification(message, "SUPPLIER");
    }

    private void saveNotification(String message, String type) {
        Notification notification = Notification.builder()
                .message(message)
                .type(type)
                .status("SENT")
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
        System.out.println("Notification Saved: " + message);
    }


}
