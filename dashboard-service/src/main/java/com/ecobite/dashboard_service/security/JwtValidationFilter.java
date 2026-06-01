package com.ecobite.dashboard_service.security;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtValidationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse,
            FilterChain chain)
            throws ServletException, IOException {

        String path = httpRequest.getRequestURI();

        System.out.println("REQUEST PATH: " + path);

        // ====================================
        // Allow WebSocket requests
        // ====================================
        if (path.startsWith("/ws")) {

            chain.doFilter(httpRequest, httpResponse);

            return;
        }

        String header =
                httpRequest.getHeader("Authorization");

        if (header != null &&
                header.startsWith("Bearer ")) {

            String token = header.substring(7);

            try {

                if (!jwtService.validateToken(token)) {

                    httpResponse.setStatus(
                            HttpServletResponse.SC_UNAUTHORIZED);

                    httpResponse.getWriter()
                            .write("Invalid or Expired Token");

                    return;
                }

                String username =
                        jwtService.extractUsername(token);

                String role =
                        jwtService.extractRole(token);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(
                                        new SimpleGrantedAuthority(
                                                "ROLE_" + role
                                        )
                                )
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

            } catch (Exception e) {

                e.printStackTrace();

                httpResponse.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED);

                httpResponse.getWriter()
                        .write("Invalid Token");

                return;
            }
        }

        chain.doFilter(httpRequest, httpResponse);
    }

}