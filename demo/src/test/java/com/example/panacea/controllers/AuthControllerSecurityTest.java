package com.example.panacea.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSecurityHeaders() throws Exception {
        mockMvc.perform(get("/api/v1/auth/test"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Content-Security-Policy"))
                .andExpect(header().exists("X-Frame-Options"))
                .andExpect(header().exists("Strict-Transport-Security"))
                .andExpect(header().exists("X-Content-Type-Options"))
                .andExpect(header().exists("X-XSS-Protection"));
        
        // Testing individual header values
        mockMvc.perform(get("/api/v1/auth/test"))
                .andExpect(header().string("Content-Security-Policy", "default-src 'self'; script-src 'self'; object-src 'none'; style-src 'self' 'unsafe-inline'; font-src 'self'; img-src 'self' data:; frame-ancestors 'none';"));
        
        mockMvc.perform(get("/api/v1/auth/test"))
                .andExpect(header().string("X-Frame-Options", "DENY"));
        
        mockMvc.perform(get("/api/v1/auth/test"))
                .andExpect(header().string("Strict-Transport-Security", "max-age=31536000; includeSubDomains"));
        
        mockMvc.perform(get("/api/v1/auth/test"))
                .andExpect(header().string("X-Content-Type-Options", "nosniff"));
        
        mockMvc.perform(get("/api/v1/auth/test"))
                .andExpect(header().string("X-XSS-Protection", "1; mode=block"));
        
        mockMvc.perform(get("/api/v1/auth/test"))
                .andExpect(header().string("Referrer-Policy", "strict-origin-when-cross-origin"));
        
        mockMvc.perform(get("/api/v1/auth/test"))
                .andExpect(header().string("Permissions-Policy", "geolocation=(), microphone=(), camera=(), payment=(), usb=()"));
    }
}