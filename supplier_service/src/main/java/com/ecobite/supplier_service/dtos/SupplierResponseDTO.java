package com.ecobite.supplier_service.dtos;

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
