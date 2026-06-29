package com.ecobite.auth_service.service;


import com.ecobite.auth_service.dto.request.*;
import com.ecobite.auth_service.dto.response.AuthResponse;
import com.ecobite.auth_service.dto.response.RegisterResponse;
import com.ecobite.auth_service.dto.response.UserResponse;

import java.util.List;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);

    Object login(LoginRequest request);

    String changePassword(ChangePasswordRequest request);

    String unlockAccount(String username);

    String forgotPassword(ForgotPasswordRequest request);

    String resetPassword(ResetPasswordRequest request);

    String logout(String token);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    String updateUser(Long id, UpdateUserRequest request);

    String deleteUser(Long id);

    // String logout(String username);


}
