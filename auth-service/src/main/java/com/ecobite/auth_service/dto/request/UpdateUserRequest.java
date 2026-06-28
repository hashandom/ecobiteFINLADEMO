package com.ecobite.auth_service.dto.request;

import com.ecobite.auth_service.enums.Permission;
import com.ecobite.auth_service.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateUserRequest {
    private String email;

    private Role role;

    private String status;

    private boolean locked;

    private Set<Permission> permissions;


}
