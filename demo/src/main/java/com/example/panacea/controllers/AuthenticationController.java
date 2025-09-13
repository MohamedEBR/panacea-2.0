package com.example.panacea.controllers;

import com.example.panacea.dto.*;
import com.example.panacea.services.AuthService;
import com.example.panacea.services.PasswordService;
import com.example.panacea.services.SecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService service;
    private final PasswordService passwordService;
    private final SecurityService securityService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        try {
            // Validate input for XSS
            if (!securityService.sanitizeInput(request.getEmail()).equals(request.getEmail()) ||
                !securityService.sanitizeInput(request.getName()).equals(request.getName()) ||
                !securityService.sanitizeInput(request.getLastName()).equals(request.getLastName())) {
                securityService.logSecurityEvent("XSS_ATTEMPT_REGISTER", request.getEmail(), 
                    "Attempted XSS in registration data");
                return ResponseEntity.badRequest().body("Invalid input detected");
            }
            
            // Validate email format
            if (!securityService.isValidEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body("Invalid email format");
            }
            
            // Validate password complexity
            if (!securityService.isValidPassword(request.getPassword())) {
                return ResponseEntity.badRequest().body(securityService.getPasswordPolicyMessage());
            }
            
            AuthenticationResponse response = service.register(request);
            securityService.logSecurityEvent("USER_REGISTERED", request.getEmail(), "New user registered");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            securityService.logSecurityEvent("REGISTRATION_ERROR", request.getEmail(), 
                "Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        try {
            // Check if account is locked
            if (securityService.isAccountLocked(request.getEmail())) {
                securityService.logSecurityEvent("LOCKED_ACCOUNT_ACCESS", request.getEmail(), 
                    "Attempted access to locked account");
                return ResponseEntity.status(HttpStatus.LOCKED).body("Account is locked due to too many failed attempts");
            }
            
            // Validate input for XSS
            if (!securityService.sanitizeInput(request.getEmail()).equals(request.getEmail())) {
                securityService.logSecurityEvent("XSS_ATTEMPT_LOGIN", request.getEmail(), 
                    "Attempted XSS in login data");
                return ResponseEntity.badRequest().body("Invalid input detected");
            }
            
            AuthenticationResponse response = service.authenticate(request);
            securityService.resetFailedAttempts(request.getEmail());
            securityService.logSecurityEvent("USER_LOGIN", request.getEmail(), "User successfully logged in");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            securityService.recordFailedAttempt(request.getEmail());
            securityService.logSecurityEvent("LOGIN_FAILED", request.getEmail(), 
                "Login failed: " + e.getMessage());
            return ResponseEntity.badRequest().body("Authentication failed");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordService.forgotPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        passwordService.logout(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    public ResponseEntity<Void> testEndpoint() {
        return ResponseEntity.ok().build();
    }
}
