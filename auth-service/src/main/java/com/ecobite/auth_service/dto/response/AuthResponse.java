package com.ecobite.auth_service.dto.response;

import com.ecobite.auth_service.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class AuthResponse {
    private String token;
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String message;

    public AuthResponse(String token,String username,Role role){
        this.token=token;
        this.username=username;
        this.role=role;
    }

    public String getToken(){ return token; }

    public String getUsername(){ return username; }

    public Role getRole(){ return role; }




}
