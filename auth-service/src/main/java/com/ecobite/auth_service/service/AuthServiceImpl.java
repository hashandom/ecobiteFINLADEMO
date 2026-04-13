package com.ecobite.auth_service.service;

import com.ecobite.auth_service.dto.request.ChangePasswordRequest;
import com.ecobite.auth_service.dto.request.LoginRequest;
import com.ecobite.auth_service.dto.request.RegisterRequest;
import com.ecobite.auth_service.dto.response.AuthResponse;
import com.ecobite.auth_service.entity.User;
import com.ecobite.auth_service.repository.UserRepository;
import com.ecobite.auth_service.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public String register(RegisterRequest request) {

        if(repository.existsByUsername(request.getUsername())){
            throw new RuntimeException("Username already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(request.getRole());
        user.setStatus("ACTIVE");
        user.setLocked(false);

        repository.save(user);

        return "User registered successfully";
    }

    @Override
    public Object login(LoginRequest request) {

        User user = repository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        if(!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )){
            throw new RuntimeException("Invalid password");
        }

        if(user.isLocked()){
            throw new RuntimeException("Account locked");
        }

        String token = jwtService.generateToken(
                user.getUsername(),
                String.valueOf(user.getRole())
        );

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getRole()
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

        repository.save(user);

        return "Password updated successfully";
    }
}
