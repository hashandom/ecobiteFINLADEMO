package com.ecobite.product_service.product_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private String category;
    private int stock;
    private int reorderLevel;
    private double unitPrice;
}
