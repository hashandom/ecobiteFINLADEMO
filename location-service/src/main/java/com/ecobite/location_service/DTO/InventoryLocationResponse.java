package com.ecobite.location_service.DTO;

import lombok.Data;

@Data
public class InventoryLocationResponse {
    private Long batchId;

    private Long locationId;

    private String locationCode;

    private String warehouse;

    private String section;

    private String shelf;

    private Integer quantity;
}
