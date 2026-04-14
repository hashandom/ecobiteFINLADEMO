package com.ecobite.api_gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtGatewayFilter implements GlobalFilter {
    @Autowired
    private JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        System.out.println("Gateway Request Path: " + path);

        String header = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        System.out.println("Authorization Header at Gateway: " + header);

        if (path.contains("/auth/login") || path.contains("/auth/register")) {
            return chain.filter(exchange);
        }

        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Missing Authorization Header");
        }

        String token = header.substring(7);
        System.out.println("Gateway Token: " + token);

        jwtService.extractUsername(token);

        return chain.filter(exchange);
    }
    }

