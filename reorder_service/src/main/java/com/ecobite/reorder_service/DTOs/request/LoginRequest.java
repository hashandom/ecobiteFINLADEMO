package com.ecobite.reorder_service.DTOs.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
