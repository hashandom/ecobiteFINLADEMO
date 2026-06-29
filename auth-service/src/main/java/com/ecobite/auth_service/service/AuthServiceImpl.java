package com.ecobite.auth_service.service;

import com.ecobite.auth_service.dto.request.*;
import com.ecobite.auth_service.dto.response.AuthResponse;
import com.ecobite.auth_service.dto.response.RegisterResponse;
import com.ecobite.auth_service.dto.response.UserResponse;
import com.ecobite.auth_service.entity.BlackListedToken;
import com.ecobite.auth_service.entity.User;
import com.ecobite.auth_service.repository.BlackListedTokenRepository;
import com.ecobite.auth_service.repository.UserRepository;
import com.ecobite.auth_service.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BlackListedTokenRepository blackListedTokenRepository;
    @Override
    public RegisterResponse register(RegisterRequest request) {

        if(repository.existsByUsername(request.getUsername())){
            throw new RuntimeException("Username already exists");
        }

        if(repository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        String tempPassword =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0,4)
                        +
                        UUID.randomUUID()
                                .toString()
                                .replace("-", "")
                                .substring(0,4)
                                .toUpperCase();

        user.setPassword(
                passwordEncoder.encode(tempPassword)
        );
        user.setRole(request.getRole());
        user.setPermissions(request.getPermissions());
        user.setStatus("ACTIVE");
        user.setLocked(false);
        user.setFirstLogin(true);
        user.setFailedAttempts(0);
        repository.save(user);

        return new RegisterResponse(
                user.getUsername(),
                tempPassword,
                "User registered successfully"
        );
    }

    @Override
    public Object login(LoginRequest request) {
        User user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(user.isLocked()){
            throw new RuntimeException("Account is locked. Contact admin.");
        }

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){

            int attempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(attempts);

            if(attempts >= 3){
                user.setLocked(true);
            }


            repository.save(user);

            throw new RuntimeException("Invalid password");
        }

        if(user.getFailedAttempts() > 0){
            user.setFailedAttempts(0);
            repository.save(user);
        }

        String token = jwtService.generateToken(
                user.getUsername(),
                user.getRole().name(),
                user.getPermissions()
        );

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getRole(),
                user.isFirstLogin()
        );
    }

    @Override
    public String changePassword(ChangePasswordRequest request) {

        User user = repository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        if(!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword()
        )){
            throw new RuntimeException("Old password incorrect");
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );
        user.setFirstLogin(false);
        repository.save(user);
        return "Password updated successfully";
    }

    @Transactional
    public String unlockAccount(String username){

        User user = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setLocked(false);
        user.setFailedAttempts(0);
        repository.save(user);
        return "Account unlocked";
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest request) {

        if (request.getEmail() == null) {
            throw new RuntimeException("Email is required");
        }
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusMinutes(15));
        repository.save(user);
        return "Password reset token: " + token;
    }

    @Override
    public String resetPassword(ResetPasswordRequest request) {
        User user = repository.findByResetToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if(user.getTokenExpiry().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token expired");
        }
        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );
        user.setResetToken(null);
        user.setTokenExpiry(null);
        user.setFirstLogin(false);
        repository.save(user);
        return "Password reset successful";
    }

    @Override
    public String logout(String token) {

        token = token.substring(7); // remove "Bearer "

        BlackListedToken blacklistedToken = new BlackListedToken();
        blacklistedToken.setToken(token);

        blackListedTokenRepository.save(blacklistedToken);

        return "Logout successful";
    }

    public List<UserResponse> getAllUsers() {

        return repository.findAll()
                .stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .permissions(user.getPermissions())
                        .status(user.getStatus())
                        .firstLogin(user.isFirstLogin())
                        .locked(user.isLocked())
                        .build())
                .toList();
    }

    public UserResponse getUserById(Long id){

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .permissions(user.getPermissions())
                .firstLogin(user.isFirstLogin())
                .status(user.getStatus())
                .locked(user.isLocked())
                .build();
    }

    public String updateUser(Long id,
                             UpdateUserRequest request){

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        boolean roleChanged =
                !user.getRole().equals(request.getRole());

        boolean permissionChanged =
                !user.getPermissions()
                        .equals(request.getPermissions());

        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setStatus(request.getStatus());
        user.setLocked(request.isLocked());
        user.setPermissions(request.getPermissions());

        repository.save(user);

        if(roleChanged || permissionChanged){
            return "User updated successfully. User must login again for permission changes to take effect.";
        }

        return "User updated successfully";
    }

    public String deleteUser(Long id){

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        repository.delete(user);

        return "User deleted successfully";
    }
}
