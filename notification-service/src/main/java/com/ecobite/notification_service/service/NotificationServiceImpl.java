package com.ecobite.notification_service.service;

import com.ecobite.notification_service.dto.event.BatchEvent;
import com.ecobite.notification_service.dto.event.ProductEvent;
import com.ecobite.notification_service.dto.event.ReorderEvent;
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

            String message =
                    "Batch " + event.getBatchId() +
                            " of " + event.getProductName() +
                            " expires on " + event.getExpiryDate();

            saveNotification(
                    message,
                    "EXPIRY_ALERT",
                    "MANAGER"
            );
        }

        else if ("BATCH_CREATED".equals(event.getEventType())) {

            String message =
                    "New batch created: "
                            + event.getBatchId()
                            + " for "
                            + event.getProductName();

            saveNotification(
                    message,
                    "BATCH_CREATED",
                    "ADMIN"
            );
        }

        else if ("STOCK_REDUCED".equals(event.getEventType())) {

            String message =
                    "Stock reduced for batch "
                            + event.getBatchId()
                            + " of "
                            + event.getProductName()
                            + ". Remaining quantity: "
                            + event.getRemainingQuantity();

            saveNotification(
                    message,
                    "REORDER_REQUIRED",
                    "ADMIN"
            );
        }
    }

    @Override
    public void handleProductEvent(ProductEvent event) {
        switch (event.getEventType()) {

            case "PRODUCT_CREATED":

                String createdMessage =
                        " New product created: "
                                + event.getProductName();

                saveNotification(createdMessage, "PRODUCT_CREATED",
                        "ADMIN");

                break;

            case "LOW_STOCK":

                String lowStockMessage =
                        "Low stock for "
                                + event.getProductName()
                                + " remaining: "
                                + event.getStock()
                                + " (Reorder Level: "
                                + event.getReorderLevel()
                                + ")";

                saveNotification(lowStockMessage, "STOCK_ALERT",
                        "MANAGER");

                break;

            default:
                System.out.println(
                        "Unknown product event: "
                                + event.getEventType()
                );
        }
    }

    @Override
    public void handleSupplierEvent(SupplierEvent event) {
        String message = "New Supplier Added: " + event.getSupplierName();
        saveNotification(message,  "SUPPLIER_CREATED",
                "ADMIN");
    }

    public void handleReorderEvent(ReorderEvent event) {

        String message =
                "Reorder created for "
                        + event.getProductName()
                        + ". Best supplier selected: "
                        + event.getSupplierName()
                        + ". Quantity: "
                        + event.getQuantity();

        saveNotification(
                message,
                "REORDER_CREATED",
                "ADMIN"
        );
    }
    private void saveNotification(String message, String type, String targetRole) {


        Notification notification = Notification.builder()
                .message(message)
                .type(type)
                .targetRole(targetRole)
                .status("SENT")
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        System.out.println("Notification Saved: " + message);
    }


    @Override
    public List<Notification>
    getNotificationsByRole(String role) {

        return notificationRepository
                .findByTargetRoleOrderByCreatedAtDesc(
                        role
                );
    }


    @Override
    public Long getUnreadCount(
            String role) {

        return notificationRepository
                .countByTargetRoleAndIsReadFalse(
                        role
                );
    }


    @Override
    public Long getTotalCount(
            String role) {

        return notificationRepository
                .countByTargetRole(
                        role
                );
    }


    @Override
    public void markAsRead(Long id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Notification not found"
                                )
                        );

        notification.setIsRead(true);

        notificationRepository.save(
                notification
        );
    }
}