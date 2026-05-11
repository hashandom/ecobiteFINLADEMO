package com.ecobite.dashboard_service.dto.external;

import lombok.Data;

@Data
public class ProductResponse {
    private Long id;
    private String productName;
    private Integer quantity;
    private String category;
}
