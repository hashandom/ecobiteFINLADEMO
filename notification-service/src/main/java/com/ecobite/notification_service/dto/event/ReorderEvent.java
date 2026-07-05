package com.ecobite.notification_service.dto.event;

import lombok.Data;

@Data
public class ReorderEvent {
    private String productId;
    private Long supplierId;
    private int quantity;
    private String status;
    private String message;
    private long timestamp;
    private String productName;
    private String supplierName;

}
