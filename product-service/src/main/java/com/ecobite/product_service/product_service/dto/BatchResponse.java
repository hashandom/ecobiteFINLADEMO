package com.ecobite.product_service.product_service.dto;

import lombok.Data;

@Data
public class BatchResponse {
    private Long id;
    private String productId;
    private String batchNumber;
}
