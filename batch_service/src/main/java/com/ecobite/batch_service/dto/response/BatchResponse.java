package com.ecobite.batch_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class BatchResponse {

    private Long id;

    private String batchNumber;

    private String productId;

    private Long supplierId;

    private Long locationId;

    private int quantity;

    private int remainingQuantity;

    private LocalDate manufactureDate;

    private LocalDate expiryDate;

    private BigDecimal purchasePrice;

    private String status;

    private String productName;
}
