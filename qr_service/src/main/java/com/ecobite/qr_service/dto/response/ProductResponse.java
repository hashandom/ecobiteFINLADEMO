package com.ecobite.qr_service.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductResponse {

    private String id;
    private String name;
    private String category;
    private Integer stock;
    private Integer reorderLevel;
    private Double unitPrice;
}
