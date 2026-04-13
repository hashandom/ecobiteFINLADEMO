package com.ecobite.auth_service.service;


import com.ecobite.auth_service.dto.request.*;
import com.ecobite.auth_service.dto.response.AuthResponse;

public interface AuthService {
    String register(RegisterRequest request);

    Object login(LoginRequest request);

    String changePassword(ChangePasswordRequest request);

    String unlockAccount(String username);

    String forgotPassword(ForgotPasswordRequest request);

    String resetPassword(ResetPasswordRequest request);

    String logout(String token);

    // String logout(String username);


}
