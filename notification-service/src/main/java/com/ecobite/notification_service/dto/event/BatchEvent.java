package com.ecobite.notification_service.dto.event;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BatchEvent {
    private String eventType; // EXPIRING_SOON
    private String productName;
    private String batchId;
    private LocalDate expiryDate;
}
