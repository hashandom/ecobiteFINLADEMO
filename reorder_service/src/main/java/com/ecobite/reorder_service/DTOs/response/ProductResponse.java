package com.ecobite.reorder_service.DTOs.response;

import lombok.Data;

@Data
public class ProductResponse {
    private String id;
    private String name;
    private String category;
    private int stock;
    private int reorderLevel;
}
