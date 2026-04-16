package com.ecobite.batch_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtValidationFilter jwtValidationFilter;

    public SecurityConfig(JwtValidationFilter jwtValidationFilter) {
        this.jwtValidationFilter = jwtValidationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtValidationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth

//                        .requestMatchers("/products").hasAnyRole("ADMIN","MANAGER","STAFF")
//
//                        .requestMatchers("/products/{id}")
//                        .hasAnyRole("ADMIN","MANAGER","STAFF")
//
//                        .requestMatchers("/products/search")
//                        .hasAnyRole("ADMIN","MANAGER","STAFF")
//
//                        .requestMatchers("/products/category/**")
//                        .hasAnyRole("ADMIN","MANAGER","STAFF")
//
//                        .requestMatchers("/products/low-stock")
//                        .hasAnyRole("ADMIN","MANAGER")
//
//                        .requestMatchers("/products/update-stock/**")
//                        .hasAnyRole("ADMIN","MANAGER")
//
//                        .requestMatchers("/products/**")
//                        .hasRole("ADMIN")
//
                         .requestMatchers("/products/**")
                                .hasAnyRole("ADMIN","MANAGER","STAFF")

                        .anyRequest()
                        .authenticated()
                );

        return http.build();
    }
}
