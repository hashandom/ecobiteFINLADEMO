package com.ecobite.batch_service.dto.request;

import lombok.Data;

@Data
public class AllocateBatchRequest {
    private String productId;
    private int quantity;
}
