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
                        .requestMatchers("/auth/forgot-password").permitAll()
                        .requestMatchers("/auth/reset-password").permitAll()

//                        // REGISTER
//                        .requestMatchers(HttpMethod.POST,"/auth/register")
//                        .hasAnyRole("ADMIN","MANAGER")
//
//                        // CHANGE PASSWORD (allow all roles)
//                        .requestMatchers(HttpMethod.PUT,"/auth/change-password")
//                        .hasAnyRole("ADMIN","MANAGER","STAFF")
//
//                        // UNLOCK
//                        .requestMatchers("/auth/unlock")
//                        .hasAnyRole("ADMIN","MANAGER")
//
//                        // ADMIN operations
//                        .requestMatchers(HttpMethod.DELETE,"/**")
//                        .hasRole("ADMIN")
//
//                        .requestMatchers(HttpMethod.POST,"/**")
//                        .hasRole("ADMIN")
//
//                        // MANAGER update
//                        .requestMatchers(HttpMethod.PUT,"/**")
//                        .hasAnyRole("ADMIN","MANAGER")
//
//                        // READ access
//                        .requestMatchers(HttpMethod.GET,"/**")
//                        .hasAnyRole("ADMIN","MANAGER","STAFF")

                        .anyRequest().authenticated()
                )

                // Add JWT filter
                .addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}