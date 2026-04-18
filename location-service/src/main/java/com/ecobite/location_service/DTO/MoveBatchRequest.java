package com.ecobite.location_service.DTO;

import lombok.Data;

@Data
public class MoveBatchRequest {
    private Long batchId;
    private Long fromLocationId;
    private Long toLocationId;
    private Integer quantity;
}
