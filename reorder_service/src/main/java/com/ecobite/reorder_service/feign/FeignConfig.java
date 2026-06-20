package com.ecobite.reorder_service.feign;

import com.ecobite.reorder_service.service.SystemTokenService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class FeignConfig {

    private final SystemTokenService tokenService;


    public FeignConfig(SystemTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {

        return new RequestInterceptor() {

            @Override
            public void apply(RequestTemplate requestTemplate) {

                ServletRequestAttributes attributes =
                        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                //  Case 1: Normal API request (user token)
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    String authHeader = request.getHeader("Authorization");

                    if (authHeader != null && !authHeader.isEmpty()) {
                        requestTemplate.header("Authorization", authHeader);
                        return;
                    }
                }

                // Case 2: Scheduler (no request context)
                requestTemplate.header(
                        "Authorization",
                        "Bearer " + tokenService.getToken()
                );
            }
        };
    }
    }

