package com.ecobite.api_gateway.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtService {
    private static final String SECRET =
            "ecobite-secret-key-ecobite-secret-key-ecobite";

    private Key key(){
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String extractUsername(String token){

        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
