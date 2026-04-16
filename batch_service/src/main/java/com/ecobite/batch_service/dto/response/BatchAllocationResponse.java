package com.ecobite.batch_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BatchAllocationResponse {
    private Long batchId;

    private String batchNumber;

    private int allocatedQuantity;
}

