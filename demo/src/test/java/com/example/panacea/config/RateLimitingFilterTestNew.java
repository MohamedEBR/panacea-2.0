package com.example.panacea.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class RateLimitingFilterTestNew {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRateLimitingWorks() throws Exception {
        // Test data
        String requestBody = """
            {
                "username": "testuser",
                "password": "testpassword"
            }
            """;

        // Make exactly 11 requests to test rate limiting
        for (int i = 0; i < 11; i++) {
            if (i < 10) {
                // First 10 requests should pass through the rate limiter
                // (they may still fail due to authentication, but not due to rate limiting)
                mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType("application/json")
                        .content(requestBody));
            } else {
                // 11th request should be rate limited
                mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType("application/json")
                        .content(requestBody))
                        .andExpect(status().is(429))
                        .andExpect(content().string("Too many requests. Please try again later."));
            }
        }
    }
}