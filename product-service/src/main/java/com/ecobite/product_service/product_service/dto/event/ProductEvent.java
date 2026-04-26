package com.ecobite.product_service.product_service.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEvent {
    private String eventType;   // LOW_STOCK
    private String productId;
    private String productName;
    private int stock;
    private int reorderLevel;
}
