package com.ecobite.reorder_service.service;

import com.ecobite.reorder_service.DTOs.request.LoginRequest;
import com.ecobite.reorder_service.DTOs.response.ApiResponse;
import com.ecobite.reorder_service.feign.AuthClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SystemTokenService {
    private final AuthClient authClient;

    @Value("${system.username}")
    private String username;

    @Value("${system.password}")
    private String password;

    private String token;

    public SystemTokenService(AuthClient authClient) {
        this.authClient = authClient;
    }

    public String getToken() {

        if (token == null) {

            LoginRequest request = new LoginRequest();
            request.setUsername(username);
            request.setPassword(password);

            ApiResponse response = authClient.login(request);

            token = response.getData().getToken();

            System.out.println("TOKEN GENERATED: " + token);
        }

        return token;
    }
}
