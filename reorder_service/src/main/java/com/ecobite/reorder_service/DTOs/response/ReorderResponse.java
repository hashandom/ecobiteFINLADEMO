package com.ecobite.reorder_service.DTOs.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReorderResponse {

    private Long id;
    private String productId;
    private Long supplierId;
    private int quantity;
    private String status;
    private LocalDateTime createdAt;
}
