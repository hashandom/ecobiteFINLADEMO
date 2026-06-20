package com.ecobite.reorder_service.feign;

import com.ecobite.reorder_service.DTOs.request.LoginRequest;
import com.ecobite.reorder_service.DTOs.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service")
public interface AuthClient {
    @PostMapping("/auth/login")
    ApiResponse login(
            @RequestBody LoginRequest request
    );
}
