package com.ecobite.api_gateway.security;

import com.ecobite.api_gateway.repository.BlackListedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtGatewayFilter implements GlobalFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private BlackListedTokenRepository blacklistRepo;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        System.out.println("Gateway Request Path: " + path);

        // Allow CORS preflight requests
        if (exchange.getRequest().getMethod().name().equals("OPTIONS")) {
            return chain.filter(exchange);
        }

        // Allow public endpoints
        if (path.startsWith("/auth/login") ||
                path.startsWith("/auth/register") ||
                path.startsWith("/auth/forgot-password") ||
                path.startsWith("/auth/reset-password")) {

            return chain.filter(exchange);
        }

        String header = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        System.out.println("Authorization Header at Gateway: " + header);

        //  Missing header
        if (header == null || !header.startsWith("Bearer ")) {
            return handleUnauthorized(exchange, "Missing Authorization Header");
        }

        String token = header.substring(7);
        System.out.println("Gateway Token: " + token);

        //  Check blacklist
        if (blacklistRepo.existsByToken(token)) {
            return handleUnauthorized(exchange, "Token is logged out");
        }

        // Validate JWT
        try {
            String username = jwtService.extractUsername(token);
            System.out.println("Valid token for user: " + username);
        } catch (Exception e) {
            return handleUnauthorized(exchange, "Invalid or expired token");
        }

        //  Forward request
        return chain.filter(exchange);
    }

    // THIS MUST BE OUTSIDE filter()
    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message) {

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        String body = """
        {
            "status": false,
            "message": "%s",
            "data": null
        }
        """.formatted(message);

        byte[] bytes = body.getBytes();
        DataBuffer buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(bytes);

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
    }

