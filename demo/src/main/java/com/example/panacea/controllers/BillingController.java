package com.example.panacea.controllers;

import com.example.panacea.models.BillingRecord;
import com.example.panacea.repo.BillingRecordRepository;
import com.example.panacea.services.DataOwnershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class BillingController {

    private final BillingRecordRepository billingRecordRepository;
    private final DataOwnershipService dataOwnershipService;

    @GetMapping("/{id}/billing")
    public ResponseEntity<?> getBilling(@PathVariable Long id) {
        dataOwnershipService.validateMemberAccess(id);
    List<BillingRecord> records = billingRecordRepository.findByMemberIdOrderByDateDesc(id);
    List<BillingDto> dto = records.stream().map(r -> new BillingDto(
        r.getId(), r.getAmount(), r.getDate(), r.getStripeTransactionId()
    )).toList();
    return ResponseEntity.ok(dto);
    }

    public record BillingDto(Long id, BigDecimal amount, LocalDateTime date, String stripeTransactionId) {}
}
