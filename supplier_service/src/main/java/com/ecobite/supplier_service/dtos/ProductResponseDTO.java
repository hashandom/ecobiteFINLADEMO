package com.ecobite.supplier_service.dtos;

import lombok.Data;

@Data
public class ProductResponseDTO {
    private String id;
    private String name;
    private Double price;
}
