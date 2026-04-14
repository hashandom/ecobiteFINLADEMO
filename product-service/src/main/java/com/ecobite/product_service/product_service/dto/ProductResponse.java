package com.ecobite.product_service.product_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private String id;
    private String name;
    private String category;
    private int stock;
    private int reorderLevel;
    private double unitPrice;
}
