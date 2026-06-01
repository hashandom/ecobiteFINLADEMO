package com.ecobite.dashboard_service.dto.event;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BatchEvent {
    private String eventType; // EXPIRING_SOON
    private String productName;
    private Long batchId;
    private LocalDate expiryDate;
    private int remainingQuantity;
}
