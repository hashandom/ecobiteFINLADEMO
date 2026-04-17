package com.ecobite.supplier_service.DTOs;

import lombok.Data;

@Data
public class SupplierRequestDTO {
    private String name;
    private String contactEmail;
    private String phone;
}
