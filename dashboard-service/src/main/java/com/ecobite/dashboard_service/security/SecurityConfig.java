package com.ecobite.dashboard_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtValidationFilter jwtValidationFilter;

    public SecurityConfig(
            JwtValidationFilter jwtValidationFilter) {

        this.jwtValidationFilter = jwtValidationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/ws/**",
                                "/ws/*",
                                "/test/**",
                                "/topic/**"
                        ).permitAll()

                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(
                        jwtValidationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
