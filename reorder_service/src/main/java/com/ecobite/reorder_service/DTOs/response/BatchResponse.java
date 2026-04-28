package com.ecobite.reorder_service.DTOs;

import lombok.Data;

@Data
public class BatchResponse {
    private String productId;
    private String batchNumber;
    private int quantity;
    private String manufactureDate;
    private String expiryDate;
    private Long supplierId;
    private double purchasePrice;
    private int remainingQuantity;
    private String status;
}
