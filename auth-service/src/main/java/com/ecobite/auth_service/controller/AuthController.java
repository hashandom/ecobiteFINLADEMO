package com.ecobite.auth_service.controller;

import com.ecobite.auth_service.dto.request.*;
import com.ecobite.auth_service.dto.response.ApiResponse;
import com.ecobite.auth_service.dto.response.AuthResponse;
import com.ecobite.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    // ================= USER MANAGEMENT =================

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or hasAuthority('USER_READ')")
    @GetMapping("/users")
    public ApiResponse getAllUsers() {

        return new ApiResponse(
                true,
                "Users retrieved successfully",
                service.getAllUsers()
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or hasAuthority('USER_READ')")
    @GetMapping("/users/{id}")
    public ApiResponse getUserById(
            @PathVariable Long id) {

        return new ApiResponse(
                true,
                "User retrieved successfully",
                service.getUserById(id)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or hasAuthority('USER_UPDATE')")
    @PutMapping("/users/{id}")
    public ApiResponse updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {

        return new ApiResponse(
                true,
                service.updateUser(id, request),
                null
        );
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('USER_DELETE')")
    @DeleteMapping("/users/{id}")
    public ApiResponse deleteUser(
            @PathVariable Long id) {

        return new ApiResponse(
                true,
                service.deleteUser(id),
                null
        );
    }

    // ================= USER REGISTRATION =================

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or hasAuthority('USER_CREATE')")
    @PostMapping("/register")
    public ApiResponse register(
            @RequestBody RegisterRequest request) {

        return new ApiResponse(
                true,
                service.register(request),
                null
        );
    }

    // ================= AUTHENTICATION =================

    @PostMapping("/login")
    public ApiResponse login(
            @RequestBody LoginRequest request) {

        return new ApiResponse(
                true,
                "Login successful",
                service.login(request)
        );
    }

    @PostMapping("/logout")
    public ApiResponse logout(
            @RequestHeader("Authorization") String token) {

        service.logout(token);

        return new ApiResponse(
                true,
                "Logout successful",
                null
        );
    }

    // ================= PASSWORD MANAGEMENT =================

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/change-password")
    public ApiResponse changePassword(
            @RequestBody ChangePasswordRequest request) {

        return new ApiResponse(
                true,
                service.changePassword(request),
                null
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or hasAuthority('USER_UPDATE')")
    @PutMapping("/unlock/{username}")
    public ApiResponse unlockAccount(
            @PathVariable String username) {

        service.unlockAccount(username);

        return new ApiResponse(
                true,
                "Account unlocked",
                null
        );
    }

    // ================= PASSWORD RESET =================

    @PostMapping("/forgot-password")
    public ApiResponse forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        return new ApiResponse(
                true,
                service.forgotPassword(request),
                null
        );
    }

    @PostMapping("/reset-password")
    public ApiResponse resetPassword(
            @RequestBody ResetPasswordRequest request) {

        return new ApiResponse(
                true,
                service.resetPassword(request),
                null
        );
    }


}
