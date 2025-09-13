package com.example.panacea.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
    "JWT_SECRET=dGVzdC1zZWNyZXQta2V5LXRoYXQtaXMtbG9uZy1lbm91Z2gtZm9yLWhtYWMtc2hhMjU2LWFsZ29yaXRobQ=="
})
public class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        
        userDetails = new User(
            "testuser",
            "password",
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    void shouldGenerateValidToken() {
        // When
        String token = jwtService.generateToken(userDetails);
        
        // Then
        assertThat(token).isNotNull().isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3); // JWT has 3 parts
    }

    @Test
    void shouldExtractUsernameFromToken() {
        // Given
        String token = jwtService.generateToken(userDetails);
        
        // When
        String extractedUsername = jwtService.extractUsername(token);
        
        // Then
        assertThat(extractedUsername).isEqualTo("testuser");
    }

    @Test
    void shouldValidateTokenSuccessfully() {
        // Given
        String token = jwtService.generateToken(userDetails);
        
        // Debug: Extract the token contents
        String username = jwtService.extractUsername(token);
        boolean isExpired = jwtService.isTokenExpired(token);
        
        // When
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        
        // Then
        System.out.println("Debug - Username: " + username);
        System.out.println("Debug - Is Expired: " + isExpired);
        System.out.println("Debug - Is Valid: " + isValid);
        
        assertThat(isValid).isTrue();
    }

    @Test
    void shouldRejectTokenWithWrongUsername() {
        // Given
        String token = jwtService.generateToken(userDetails);
        UserDetails differentUser = new User(
            "differentuser",
            "password",
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        
        // When
        boolean isValid = jwtService.isTokenValid(token, differentUser);
        
        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldRejectMalformedToken() {
        // Given
        String malformedToken = "invalid.token.format";
        
        // When
        boolean isValid = jwtService.isTokenValid(malformedToken, userDetails);
        
        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldRejectTokenWithTamperedSignature() {
        // Given
        String token = jwtService.generateToken(userDetails);
        // Tamper with the signature
        String tamperedToken = token.substring(0, token.lastIndexOf('.')) + ".tampered_signature";
        
        // When
        boolean isValid = jwtService.isTokenValid(tamperedToken, userDetails);
        
        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldExtractExpirationDate() {
        // Given
        String token = jwtService.generateToken(userDetails);
        
        // When
        var expiration = jwtService.extractExpiration(token);
        
        // Then
        assertThat(expiration).isNotNull();
        assertThat(expiration).isAfter(new java.util.Date());
    }

    @Test
    void shouldDetectTokenAsNotExpired() {
        // Given
        String token = jwtService.generateToken(userDetails);
        
        // When
        boolean isExpired = jwtService.isTokenExpired(token);
        
        // Then
        assertThat(isExpired).isFalse();
    }
}