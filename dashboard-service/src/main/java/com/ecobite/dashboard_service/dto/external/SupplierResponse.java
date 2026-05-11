package com.ecobite.dashboard_service.dto.external;

import lombok.Data;

@Data
public class SupplierResponse {
    private Long id;
    private String supplierName;
    private String email;
    private String phone;
}
