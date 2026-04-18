package com.ecobite.location_service.DTO;

import lombok.Data;

@Data
public class AssignBatchRequest {
    private Long batchId;
    private Long locationId;
    private Integer quantity;
}
