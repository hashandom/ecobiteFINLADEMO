package com.ecobite.auth_service.service;


import com.ecobite.auth_service.dto.request.*;
import com.ecobite.auth_service.dto.response.AuthResponse;

public interface AuthService {
    String register(RegisterRequest request);

    Object login(LoginRequest request);

    String changePassword(ChangePasswordRequest request);

   // Object refreshToken(RefreshTokenRequest request);

   // String logout(String username);

   // String forgotPassword(ForgotPasswordRequest request);

}
