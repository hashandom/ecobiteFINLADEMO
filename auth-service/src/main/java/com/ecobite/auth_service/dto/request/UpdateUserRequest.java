package com.ecobite.auth_service.dto.request;

import com.ecobite.auth_service.enums.Role;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String email;

    private Role role;

    private String status;

    private boolean locked;
}
