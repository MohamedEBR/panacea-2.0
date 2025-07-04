package com.example.panacea.services;

import com.example.panacea.enums.MemberStatus;
import com.example.panacea.models.BillingRecord;
import com.example.panacea.models.Member;
import com.example.panacea.models.Program;
import com.example.panacea.models.Student;
import com.example.panacea.repo.MemberRepository;
import com.example.panacea.exceptions.StripeIntegrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingService {

    private final MemberRepository memberRepository;
    private final StripeService stripeService;

    @Scheduled(cron = "0 0 0 1 * *")
    public void runMonthlyBillingJob() {
        log.info("Monthly billing job started");
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            if (shouldCharge(member)) {
                billMember(member);
            }
        }
        log.info("Monthly billing job completed");
    }

    public void billMember(Member member) {
        BigDecimal total = BigDecimal.ZERO;
        List<Student> billable = member.getStudents().stream()
                .filter(Student::isActive)
                .filter(s -> !s.getPrograms().isEmpty())
                .collect(Collectors.toList());

        for (Student student : billable) {
            for (Program program : student.getPrograms()) {
                total = total.add(program.getRate());
                try {
                    stripeService.createInvoiceItem(member.getStripeCustomerId(), program.getRate(),
                            String.format("Program %s for student %s", program.getName(), student.getName()));
                } catch (Exception e) {
                    log.error("Failed to create invoice item for member {}: {}", member.getEmail(), e.getMessage());
                    throw new StripeIntegrationException("Invoice item creation failed", e);
                }
            }
        }

        if (total.compareTo(BigDecimal.ZERO) > 0) {
            try {
                String invoiceId = stripeService.createInvoice(member.getStripeCustomerId());
                BillingRecord record = BillingRecord.builder()
                        .amount(total)
                        .date(LocalDateTime.now())
                        .stripeTransactionId(invoiceId)
                        .billedStudents(billable)
                        .member(member)
                        .build();
                member.getBillingHistory().add(record);
                memberRepository.save(member);
            } catch (Exception e) {
                log.error("Failed to create invoice for member {}: {}", member.getEmail(), e.getMessage());
                throw new StripeIntegrationException("Invoice creation failed", e);
            }
        }
    }

    private boolean shouldCharge(Member member) {
        return member.getStatus() == MemberStatus.ACTIVE
                && member.getStudents().stream().anyMatch(Student::isActive);
    }
}
