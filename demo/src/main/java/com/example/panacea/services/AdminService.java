package com.example.panacea.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final BillingService billingService;

    public void triggerMonthlyBilling() {
        billingService.processMonthlyBilling();
    }

    public String exportData() {
        // Placeholder for data export logic
        return "/path/to/exported/data.csv";
    }
}