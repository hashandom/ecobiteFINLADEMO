package com.ecobite.notification_service.dto.event;

import lombok.Data;

@Data
public class ProductEvent {
    private String eventType; // LOW_STOCK
    private String productName;
    private int remainingStock;
}
