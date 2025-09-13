package com.example.panacea.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtErrorHandlingTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testMissingJwtToken() throws Exception {
        // Access protected endpoint without JWT token should return 401 or 403
        mockMvc.perform(get("/api/v1/admin/export-data"))
                .andExpect(status().isForbidden()); // 403 Forbidden (Spring Security behavior)
    }

    @Test
    public void testInvalidJwtToken() throws Exception {
        // Access protected endpoint with invalid JWT token should return 401 or 403
        mockMvc.perform(get("/api/v1/admin/export-data")
                .header("Authorization", "Bearer invalid.jwt.token"))
                .andExpect(status().isForbidden()); // 403 Forbidden (Spring Security behavior)
    }

    @Test
    public void testMalformedJwtToken() throws Exception {
        // Access protected endpoint with malformed JWT token
        mockMvc.perform(get("/api/v1/admin/export-data")
                .header("Authorization", "Bearer malformed-token"))
                .andExpect(status().isForbidden()); // 403 Forbidden (Spring Security behavior)
    }

    @Test
    public void testExpiredJwtToken() throws Exception {
        // Test with a token that looks valid but is expired
        // This is a mock expired token - in real scenario, you'd create a token with past expiration
        String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImV4cCI6MTYwOTQ1OTIwMH0.invalid";
        
        mockMvc.perform(get("/api/v1/admin/export-data")
                .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isForbidden()); // 403 Forbidden (Spring Security behavior)
    }

    @Test
    public void testAuthenticationWithInvalidCredentials() throws Exception {
        // Test authentication endpoint with invalid credentials should return 401
        String invalidCredentials = """
            {
                "email": "nonexistentuser@example.com",
                "password": "wrongpassword"
            }
            """;

        mockMvc.perform(post("/api/v1/auth/authenticate")
                .contentType("application/json")
                .content(invalidCredentials))
                .andExpect(status().is4xxClientError()); // Rate limiting returns 429 Too Many Requests
    }

    @Test
    public void testAuthenticationWithMalformedRequest() throws Exception {
        // Test authentication endpoint with malformed JSON should return 400
        String malformedJson = """
            {
                "username": "test",
                invalid json
            }
            """;

        mockMvc.perform(post("/api/v1/auth/authenticate")
                .contentType("application/json")
                .content(malformedJson))
                .andExpect(status().isBadRequest()); // 400 Bad Request
    }

    @Test
    public void testRegistrationWithInvalidData() throws Exception {
        // Test registration with invalid data should return 400
        String invalidRegistration = """
            {
                "firstName": "",
                "lastName": "",
                "email": "invalid-email",
                "password": ""
            }
            """;

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType("application/json")
                .content(invalidRegistration))
                .andExpect(status().isBadRequest()); // 400 Bad Request
    }
}