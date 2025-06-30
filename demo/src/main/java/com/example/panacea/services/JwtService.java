package com.example.panacea.services;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class JwtService {


    private static final Dotenv dotenv = Dotenv.load();
    private static final String JWT_SECRET = dotenv.get("JWT_SECRET");

    public String extractUsername(String token) {
        return null;
    }

    private Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
