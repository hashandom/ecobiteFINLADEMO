package com.ecobite.reorder_service.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReorderEvent {
    private String productId;
    private Long supplierId;
    private int quantity;
    private String message;
}
