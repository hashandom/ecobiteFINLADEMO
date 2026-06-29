package com.ecobite.auth_service.dto.response;

import com.ecobite.auth_service.enums.Permission;
import com.ecobite.auth_service.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserResponse {
    private Long id;

    private String username;

    private String email;

    private Role role;

    private String status;

    private boolean locked;

    private Set<Permission> permissions;
}
