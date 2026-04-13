package com.ecobite.auth_service.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JWTValidationFilter implements Filter {

    @Autowired
    private JwtService jwtService;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest http = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String header = http.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            try {

                if (!jwtService.validateToken(token)) {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.getWriter().write("Invalid or Expired Token");
                    return;
                }

                String username = jwtService.extractUsername(token);

            } catch (Exception e) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("Invalid Token");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
