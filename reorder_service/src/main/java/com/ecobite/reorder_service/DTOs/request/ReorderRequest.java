package com.ecobite.reorder_service.DTOs;

import lombok.Data;

@Data
public class ReorderRequest {
    private String productId;
    private int quantity;
}
