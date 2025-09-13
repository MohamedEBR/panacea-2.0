package com.example.panacea.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RateLimitingFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRateLimitingEnforced() throws Exception {
        String requestBody = """
            {
                "name": "Test",
                "lastName": "User", 
                "email": "testuser@example.com",
                "password": "testpassword",
                "dob": "1990-01-01",
                "address": "123 Test St",
                "city": "Test City",
                "phone": "1234567890",
                "postalCode": "12345"
            }
            """;

        // Make 10 requests (should be allowed)
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType("application/json")
                    .content(requestBody)); // Don't check status - just ensure no rate limiting
        }

        // 11th request should be rate limited
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(status().isTooManyRequests()); // 429 Too Many Requests
    }
}