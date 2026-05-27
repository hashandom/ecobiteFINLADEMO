package com.ecobite.supplier_service.dtos.event;

import lombok.Data;

@Data
public class SupplierEvent {
    private String eventType; // CREATED
    private String supplierName;
    private Long supplierId;
}
