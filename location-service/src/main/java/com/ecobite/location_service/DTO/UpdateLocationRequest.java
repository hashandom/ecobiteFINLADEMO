package com.ecobite.location_service.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLocationRequest {
    @NotBlank(message = "Warehouse is required")
    private String warehouse;

    @NotBlank(message = "Section is required")
    private String section;

    @NotBlank(message = "Shelf is required")
    private String shelf;

    @Positive(message = "Capacity must be greater than 0")
    private Integer capacity;
}
