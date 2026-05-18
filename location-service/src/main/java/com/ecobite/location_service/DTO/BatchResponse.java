package com.ecobite.location_service.DTO;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchResponse {
    private Long id;

    private String batchNumber;

    private String productId;

    private Long supplierId;

    private Integer quantity;

    private Integer remainingQuantity;

    private LocalDate manufactureDate;

    private LocalDate expiryDate;

    private BigDecimal purchasePrice;

    private String status;
}
