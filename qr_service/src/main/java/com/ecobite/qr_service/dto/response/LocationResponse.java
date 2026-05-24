package com.ecobite.qr_service.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationResponse {
    private Long id;
    private String locationCode;
    private String warehouse;
    private String section;
    private String shelf;
    private Integer capacity;
    private Integer currentOccupancy;
}
