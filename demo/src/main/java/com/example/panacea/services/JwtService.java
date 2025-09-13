package com.example.panacea.services;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String JWT_SECRET = dotenv.get("JWT_SECRET");
    private static final String ISSUER = "com.example.panacea";
    private static final String AUDIENCE = "panacea-users";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .claim("sub", userDetails.getUsername())
                .claim("iss", ISSUER)
                .claim("aud", AUDIENCE)
                .claim("iat", new Date(System.currentTimeMillis()))
                .claim("exp", new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 minutes expiration
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            final Claims claims = extractClaims(token);
            
            boolean usernameMatch = username.equals(userDetails.getUsername());
            boolean notExpired = !isTokenExpired(token);
            boolean issuerMatch = ISSUER.equals(claims.getIssuer());
            
            // Handle audience validation - audience might be a string or collection
            boolean audienceMatch = false;
            Object audienceObj = claims.getAudience();
            if (audienceObj instanceof String) {
                audienceMatch = AUDIENCE.equals(audienceObj);
            } else if (audienceObj instanceof java.util.Collection) {
                @SuppressWarnings("unchecked")
                java.util.Collection<String> audiences = (java.util.Collection<String>) audienceObj;
                audienceMatch = audiences.contains(AUDIENCE);
            }
            
            return usernameMatch && notExpired && issuerMatch && audienceMatch;
        } catch (Exception e) {
            // Log the exception for debugging
            System.err.println("Token validation failed: " + e.getMessage());
            e.printStackTrace();
            return false; // Invalid token
        }
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractClaims(String token) {
        Jws<Claims> jws = Jwts.parser()
                .verifyWith(getSignInKey()) // Use the appropriate key
                .build()
                .parseSignedClaims(token);
        return jws.getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Base64.getDecoder().decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
