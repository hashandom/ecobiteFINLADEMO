package com.ecobite.supplier_service.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SupplierRequestDTO {
    @NotBlank(message = "Supplier name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String contactEmail;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^(07)[0-9]{8}$",
            message = "Invalid Sri Lankan phone number"
    )
    private String phone;
}
