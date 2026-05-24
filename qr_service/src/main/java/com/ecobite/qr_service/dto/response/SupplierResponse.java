package com.ecobite.qr_service.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupplierResponse {
    private Long id;
    private String name;
    private String contactEmail;
    private String phone;
    private Double rating;
}
