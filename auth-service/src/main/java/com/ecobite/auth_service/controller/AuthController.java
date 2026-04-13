package com.ecobite.auth_service.controller;

import com.ecobite.auth_service.dto.request.*;
import com.ecobite.auth_service.dto.response.ApiResponse;
import com.ecobite.auth_service.dto.response.AuthResponse;
import com.ecobite.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public ApiResponse register(@RequestBody RegisterRequest request){
        return new ApiResponse(true,
                service.register(request),
                null);
    }


    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest request){
        return new ApiResponse(true,
                "Login successful",
                service.login(request));
    }


    @PutMapping("/change-password")
    public ApiResponse changePassword(@RequestBody ChangePasswordRequest request){
        return new ApiResponse(true,
                service.changePassword(request),
                null);
    }

    @PutMapping("/unlock/{username}")
    public ApiResponse unlockAccount(@PathVariable String username){
        service.unlockAccount(username);
        return new ApiResponse(true,"Account unlocked",null);
    }

    @PostMapping("/forgot-password")
    public ApiResponse forgotPassword(@RequestBody ForgotPasswordRequest request){
        return new ApiResponse(
                true,
                service.forgotPassword(request),
                null
        );
    }

    @PostMapping("/reset-password")
    public ApiResponse resetPassword(@RequestBody ResetPasswordRequest request){
        return new ApiResponse(
                true,
                service.resetPassword(request),
                null
        );
    }

    @PostMapping("/logout")
    public ApiResponse logout(@RequestHeader("Authorization") String token){

        service.logout(token);

        return new ApiResponse(
                true,
                "Logout successful",
                null
        );
    }
}
