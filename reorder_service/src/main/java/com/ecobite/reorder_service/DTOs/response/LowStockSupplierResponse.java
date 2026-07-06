package com.ecobite.reorder_service.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LowStockSupplierResponse {
    private String productId;
    private String productName;
    private Integer currentStock;
    private Integer reorderLevel;

    private Long supplierId;
    private String supplierName;
    private String supplierEmail;
    private String supplierPhone;
}
