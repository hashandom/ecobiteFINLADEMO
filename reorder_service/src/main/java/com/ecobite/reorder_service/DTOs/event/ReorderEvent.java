package com.ecobite.reorder_service.DTOs.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReorderEvent {
    private String productId;

    private String productName;

    private Long supplierId;

    private String supplierName;

    private Integer quantity;

    private String message;
}
