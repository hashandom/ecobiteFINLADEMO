package com.ecobite.qr_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtValidationFilter jwtValidationFilter;

    public SecurityConfig(
            JwtValidationFilter jwtValidationFilter
    ) {
        this.jwtValidationFilter = jwtValidationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .addFilterBefore(
                        jwtValidationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .authorizeHttpRequests(auth -> auth

                        // Public endpoints
                        .requestMatchers("/qr/image/**")
                        .permitAll()

                        .requestMatchers("/qr/scan/**")
                        .permitAll()

                        .requestMatchers("/qr/batch/**")
                        .permitAll()

                        // Protected endpoint
                        .requestMatchers("/qr/generate")
                        .hasAnyRole(
                                "ADMIN",
                                "MANAGER",
                                "STAFF"
                        )

                        .anyRequest()
                        .authenticated()
                );

        return http.build();
    }
}
