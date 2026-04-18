package com.ecobite.location_service.DTO;

import lombok.Data;

@Data
public class InventoryLocationResponse {
    private Long batchId;
    private Long locationId;
    private Integer quantity;
}
