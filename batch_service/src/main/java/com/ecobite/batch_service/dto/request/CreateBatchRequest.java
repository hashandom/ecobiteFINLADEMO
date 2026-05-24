package com.ecobite.batch_service.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateBatchRequest {

    @NotBlank(message = "Product ID is required")
    private String productId;

    @NotBlank(message = "Batch number is required")
    private String batchNumber;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;

    @NotNull(message = "Manufacture date is required")
    private LocalDate manufactureDate;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotNull(message = "Location ID is required")
    private Long locationId;

    @DecimalMin(value = "0.1", message = "Purchase price must be greater than 0")
    private BigDecimal purchasePrice;
}
