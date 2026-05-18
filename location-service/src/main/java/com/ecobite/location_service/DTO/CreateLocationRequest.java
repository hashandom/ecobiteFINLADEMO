package com.ecobite.location_service.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateLocationRequest {
    @NotBlank(message = "Warehouse is required")
    private String warehouse;

    @NotBlank(message = "Section is required")
    private String section;

    @NotBlank(message = "Shelf is required")
    private String shelf;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be greater than 0")
    private Integer capacity;
}
