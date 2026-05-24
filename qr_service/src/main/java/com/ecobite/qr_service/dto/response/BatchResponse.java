package com.ecobite.qr_service.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class BatchResponse {
    private Long id;

    private String batchNumber;

    private String productId;

    private Long supplierId;

    private Long locationId;

    private Integer quantity;

    private Integer remainingQuantity;

    private LocalDate manufactureDate;

    private LocalDate expiryDate;

    private Double purchasePrice;

    private String status;
}
