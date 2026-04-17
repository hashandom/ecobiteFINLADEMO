package com.ecobite.supplier_service.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierResponseDTO {
    private Long id;
    private String name;
    private String contactEmail;
    private String phone;
    private Double rating;
}
