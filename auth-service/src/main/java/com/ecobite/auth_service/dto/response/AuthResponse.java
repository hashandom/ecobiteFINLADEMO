package com.ecobite.auth_service.dto.response;

import com.ecobite.auth_service.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class AuthResponse {
    private String token;

    private String username;

    private boolean firstLogin;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String message;

    public AuthResponse(
            String token,
            String username,
            Role role,
            boolean firstLogin
    ) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.firstLogin = firstLogin;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public String getMessage() {
        return message;
    }


}
