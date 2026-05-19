package com.ecobite.location_service.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MoveBatchRequest {
    @NotNull(message = "Batch ID is required")
    private Long batchId;

    @NotNull(message = "From location ID is required")
    private Long fromLocationId;

    @NotNull(message = "To location ID is required")
    private Long toLocationId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;
}
