package com.ecobite.reorder_service.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    private static final String SYSTEM_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBlcmFkbWluIiwicm9sZSI6IkFETUlOIiwiaWF0IjoxNzc4ODQwOTE2LCJleHAiOjE3Nzg5MjczMTZ9.gYnm2qNw267JBRv19hWE1CpC_5JEMQhVkJzOC8C030k";

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
                requestTemplate.header("Authorization", "Bearer " + SYSTEM_TOKEN);
            }
        };
    }
    }

