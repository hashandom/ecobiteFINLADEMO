package com.ecobite.location_service.DTO;

import lombok.Data;

@Data
public class CreateLocationRequest {
    private String warehouse;
    private String section;
    private String shelf;
    private Integer capacity;
}
