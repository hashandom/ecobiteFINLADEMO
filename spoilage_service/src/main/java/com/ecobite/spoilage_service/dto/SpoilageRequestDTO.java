package com.ecobite.spoilage_service.dto;

import com.ecobite.spoilage_service.Enum.SpoilageType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SpoilageRequestDTO {
    @NotNull(message = "Batch ID is required")
    private Long batchId;

    @NotBlank(message = "Product name is required")
    private String productName;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    @DecimalMin(value = "0.0", inclusive = false,
            message = "Unit price must be greater than 0")
    private BigDecimal unitPrice;

    @NotBlank(message = "Reason is required")
    private String reason;

    @NotNull(message = "Spoilage type is required")
    private SpoilageType spoilageType;

    @NotBlank(message = "Reported by is required")
    private String reportedBy;
}
