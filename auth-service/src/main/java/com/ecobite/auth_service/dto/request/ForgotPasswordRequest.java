package com.ecobite.auth_service.dto.request;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
}
