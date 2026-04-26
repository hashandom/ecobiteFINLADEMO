package com.ecobite.notification_service.dto.event;

import lombok.Data;

@Data
public class ProductEvent {
    private String eventType;
    private String productId;
    private String productName;
    private int stock;
    private int reorderLevel;
}
