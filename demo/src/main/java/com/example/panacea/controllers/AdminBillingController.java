package com.example.panacea.controllers;

import com.example.panacea.services.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/billing")
@RequiredArgsConstructor
public class AdminBillingController {

    private final BillingService billingService;

    @PostMapping("/run")
    @PreAuthorize("hasRole('SUPER_USER')")
    public ResponseEntity<String> runBillingNow() {
        billingService.runMonthlyBillingJob();
        return ResponseEntity.ok("Billing job triggered successfully");
    }
}
