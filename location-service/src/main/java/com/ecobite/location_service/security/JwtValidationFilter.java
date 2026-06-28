package com.ecobite.location_service.security;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class JwtValidationFilter implements Filter {

    @Autowired
    private JwtService jwtService;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String header = httpRequest.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);
            System.out.println("Authorization Header: " + header);
            System.out.println("Token Extracted: " + token);

            try {

                if (!jwtService.validateToken(token)) {
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    httpResponse.getWriter().write("Invalid or Expired Token");
                    return;
                }

                String username = jwtService.extractUsername(token);
                String role = jwtService.extractRole(token);

                List<SimpleGrantedAuthority> authorities =
                        new java.util.ArrayList<>();

// Role authority
                authorities.add(
                        new SimpleGrantedAuthority(
                                "ROLE_" + role
                        )
                );

// Permission authorities
                List<String> permissions =
                        jwtService.extractPermissions(token);

                if(permissions != null){
                    permissions.forEach(permission ->
                            authorities.add(
                                    new SimpleGrantedAuthority(permission)
                            )
                    );
                }

                System.out.println("Authorities: " + authorities);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                authorities
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("Invalid Token");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}