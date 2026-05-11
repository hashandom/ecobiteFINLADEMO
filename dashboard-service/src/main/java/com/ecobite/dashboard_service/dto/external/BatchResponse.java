package com.ecobite.dashboard_service.dto.external;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BatchResponse {
    private Long id;
    private String batchCode;
    private LocalDate expiryDate;
    private Integer quantity;
}
