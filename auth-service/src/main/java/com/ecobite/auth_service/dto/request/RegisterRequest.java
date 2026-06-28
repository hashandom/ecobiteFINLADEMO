package com.ecobite.auth_service.dto.request;

import com.ecobite.auth_service.enums.Permission;
import com.ecobite.auth_service.enums.Role;

import java.util.Set;

public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private Role role;
    private Set<Permission> permissions;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
