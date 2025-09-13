package com.example.panacea.services;

import com.example.panacea.models.BillingRecord;
import com.example.panacea.models.Member;
import com.example.panacea.models.Program;
import com.example.panacea.repo.MemberRepository;
import com.example.panacea.repo.BillingRecordRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final MemberRepository memberRepository;
    private final BillingRecordRepository billingRecordRepository;
    private final StripeService stripeService;

    @Scheduled(cron = "0 0 0 1 * ?") // Runs on the 1st of every month
    public void processMonthlyBilling() {
        List<Member> activeMembers = memberRepository.findActiveMembers();

        for (Member member : activeMembers) {
            try {
                BigDecimal totalAmount = computeTotalAmount(member);
                Session session = stripeService.createStripeSession(member, totalAmount);

                if (session.getPaymentStatus().equals("paid")) {
                    saveBillingRecord(member, totalAmount, session.getId());
                } else {
                    handlePaymentFailure(member);
                }
            } catch (StripeException e) {
                handlePaymentFailure(member);
            }
        }
    }

    private BigDecimal computeTotalAmount(Member member) {
        return member.getStudents().stream()
                .flatMap(student -> student.getPrograms().stream())
                .map(Program::getRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void saveBillingRecord(Member member, BigDecimal amount, String transactionId) {
        BillingRecord record = BillingRecord.builder()
                .member(member)
                .amount(amount)
                .date(LocalDateTime.now())
                .stripeTransactionId(transactionId)
                .build();
        billingRecordRepository.save(record);
    }

    private void handlePaymentFailure(Member member) {
        // Logic to retry payment or suspend member
    }
}