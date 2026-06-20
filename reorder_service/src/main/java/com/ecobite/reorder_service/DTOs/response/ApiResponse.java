package com.ecobite.reorder_service.DTOs.response;

import lombok.Data;

@Data
public class ApiResponse {
    private boolean status;
    private String message;
    private AuthResponse data;
}
