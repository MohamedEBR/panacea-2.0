package com.example.panacea.services;

import com.example.panacea.enums.StudentStatus;
import com.example.panacea.models.*;
import com.example.panacea.repo.*;
import com.stripe.model.checkout.Session;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StripeWebhookService {

    private final MemberRepository memberRepository;
    private final BillingRecordRepository billingRecordRepository;

    public void handleStripeEvent(Event event) {
        if ("checkout.session.completed".equals(event.getType())) {
            StripeObject dataObject = event.getDataObjectDeserializer().getObject().orElse(null);
            if (!(dataObject instanceof Session session)) return;

            String email = session.getCustomerDetails().getEmail(); // Assuming you're using email to identify the member
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Member not found for email: " + email));

            // Ideally store the session ID in the metadata for more traceability
            List<Student> billedStudents = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            for (Student student : member.getStudents()) {
                if (student.getPrograms() == null || student.getPrograms().isEmpty()) continue;
                // Activate students once payment completes
                student.setStatus(StudentStatus.ACTIVE);
                billedStudents.add(student);

                for (Program p : student.getPrograms()) {
                    total = total.add(p.getRate());
                }
            }

            BillingRecord record = BillingRecord.builder()
                    .member(member)
                    .billedStudents(billedStudents)
                    .amount(total)
                    .stripeTransactionId(session.getPaymentIntent())
                    .date(LocalDateTime.now())
                    .build();

            billingRecordRepository.save(record);
            memberRepository.save(member);
        }
    }
}
