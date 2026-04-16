package com.ecobite.batch_service.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateBatchRequest {

    private String productId;

    private String batchNumber;

    private int quantity;

    private LocalDate manufactureDate;

    private LocalDate expiryDate;

    private Long supplierId;

    private BigDecimal purchasePrice;
}
