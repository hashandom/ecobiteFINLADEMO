package com.ecobite.auth_service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JWTValidationFilter jwtValidationFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // Public endpoints
                        .requestMatchers("/auth/login").permitAll()

                        // Only ADMIN and MANAGER can register users
                        .requestMatchers("/auth/register")
                        .hasAnyRole("ADMIN","MANAGER")

                        // STAFF → VIEW ONLY
                        .requestMatchers(HttpMethod.GET, "/**")
                        .hasAnyRole("STAFF","MANAGER","ADMIN")

                        // MANAGER → UPDATE
                        .requestMatchers(HttpMethod.PUT, "/**")
                        .hasAnyRole("MANAGER","ADMIN")

                        // ADMIN → CREATE
                        .requestMatchers(HttpMethod.POST, "/**")
                        .hasRole("ADMIN")

                        // ADMIN → DELETE
                        .requestMatchers(HttpMethod.DELETE, "/**")
                        .hasRole("ADMIN")

                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                )

                // Add JWT filter
                .addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}