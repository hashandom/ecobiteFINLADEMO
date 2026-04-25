package com.ecobite.supplier_service.dtos;

import lombok.Data;

@Data
public class AssignProductRequestDTO {
    private Long supplierId;
    private String productId;
}
