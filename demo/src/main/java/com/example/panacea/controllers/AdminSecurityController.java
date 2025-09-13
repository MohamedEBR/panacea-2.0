package com.example.panacea.controllers;

import com.example.panacea.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/security")
@RequiredArgsConstructor
public class AdminSecurityController {

    private final SecurityService securityService;

    @PostMapping("/unlock")
    public ResponseEntity<String> unlock(@RequestParam String email) {
        securityService.unlockAccount(email.trim());
        return ResponseEntity.ok("Account unlocked for: " + email.trim());
    }

    @GetMapping("/locked")
    public ResponseEntity<Boolean> isLocked(@RequestParam String email) {
        return ResponseEntity.ok(securityService.isAccountLocked(email.trim()));
    }

    @GetMapping("/audit")
    public ResponseEntity<?> getAudit(@RequestParam(defaultValue = "100") int limit) {
        return ResponseEntity.ok(securityService.getAuditLogs(limit));
    }

    @GetMapping("/failed")
    public ResponseEntity<?> getFailed(@RequestParam(defaultValue = "100") int limit) {
        return ResponseEntity.ok(securityService.getFailedLoginAttempts(limit));
    }

    @GetMapping("/blacklisted")
    public ResponseEntity<?> getBlacklisted() {
        return ResponseEntity.ok(securityService.getBlacklistedTokens());
    }
}
