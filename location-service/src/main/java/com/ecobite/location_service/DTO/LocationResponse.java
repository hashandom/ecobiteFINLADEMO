package com.ecobite.location_service.DTO;

import lombok.Data;

@Data
public class LocationResponse {
    private Long id;
    private String locationCode;
    private String warehouse;
    private String section;
    private String shelf;
    private Integer capacity;
    private Integer currentOccupancy;
}
