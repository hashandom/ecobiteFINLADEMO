package com.ecobite.notification_service.dto.event;

import lombok.Data;

@Data
public class BatchEvent {
    private String eventType; // EXPIRING_SOON
    private String productName;
    private String batchId;
    private String expiryDate;
}
