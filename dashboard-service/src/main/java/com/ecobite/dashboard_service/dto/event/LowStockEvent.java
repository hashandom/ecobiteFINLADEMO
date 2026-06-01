package com.ecobite.dashboard_service.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LowStockEvent {

    private String productName;
    private int remainingQuantity;
    private String message;
}
