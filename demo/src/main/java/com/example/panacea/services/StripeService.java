package com.example.panacea.services;

import com.example.panacea.dto.CreateSubscriptionRequest;
import com.example.panacea.models.*;
import com.example.panacea.repo.*;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StripeService {

    @Value("${stripe.api.key}")
    private String secretKey;

        @Value("${app.frontend.base-url:http://localhost:5173}")
        private String frontendBaseUrl;

    private final MemberRepository memberRepository;
    private final StudentRepository studentRepository;
    private final ProgramRepository programRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

        public Session createStripeSession(CreateSubscriptionRequest request) {
                // Determine member: explicit ID or from current auth
                Member member;
                if (request != null && request.getMemberId() != null) {
                        member = memberRepository.findById(request.getMemberId())
                                        .orElseThrow(() -> new RuntimeException("Member not found"));
                } else {
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        if (auth == null) throw new RuntimeException("Not authenticated");
                        String email = auth.getName();
                        member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Member not found for email: " + email));
                }

                List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

                boolean hasExplicitSelections = request != null && request.getStudents() != null && !request.getStudents().isEmpty();
                if (!hasExplicitSelections) {
                        // Use current enrollments on student's programs
                        List<Student> students = member.getStudents();
                        if (students != null) {
                                for (Student student : students) {
                                        if (student.getPrograms() == null) continue;
                                        for (Program program : student.getPrograms()) {
                                                lineItems.add(buildLineItem(program, "Student: " + student.getName() + " - Program: " + program.getName()));
                                        }
                                }
                        }
                        } else if (request != null && request.getStudents() != null) {
                                for (CreateSubscriptionRequest.StudentProgramSelection sps : request.getStudents()) {
                                if (sps.studentId != null) {
                                        Student student = studentRepository.findById(sps.studentId)
                                                        .orElseThrow(() -> new RuntimeException("Student not found"));

                                        List<Long> programIds = sps.programIds;
                                        if (programIds == null || programIds.isEmpty()) {
                                                // If none provided, charge for existing enrollments
                                                if (student.getPrograms() != null) {
                                                        for (Program program : student.getPrograms()) {
                                                                lineItems.add(buildLineItem(program, "Student: " + student.getName() + " - Program: " + program.getName()));
                                                        }
                                                }
                                        } else {
                                                for (Long programId : programIds) {
                                                        Program program = programRepository.findById(programId)
                                                                        .orElseThrow(() -> new RuntimeException("Program not found"));
                                                        lineItems.add(buildLineItem(program, "Student: " + student.getName() + " - Program: " + program.getName()));
                                                }
                                        }
                                                } else {
                                        // No studentId provided: create generic line items from programIds
                                        if (sps.programIds != null) {
                                                for (Long programId : sps.programIds) {
                                                        Program program = programRepository.findById(programId)
                                                                        .orElseThrow(() -> new RuntimeException("Program not found"));
                                                        lineItems.add(buildLineItem(program, "Program: " + program.getName()));
                                                }
                                        }
                                }
                        }
                }

                if (lineItems.isEmpty()) {
                        throw new RuntimeException("No items to charge");
                }

        SessionCreateParams params = SessionCreateParams.builder()
                .setCustomerEmail(member.getEmail()) // optional
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendBaseUrl + "/signup/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(frontendBaseUrl + "/signup/cancel")
                .addAllLineItem(lineItems)
                .build();

        try {
            return Session.create(params);
        } catch (Exception e) {
            throw new RuntimeException("Stripe session creation failed: " + e.getMessage());
        }
    }

        private SessionCreateParams.LineItem buildLineItem(Program program, String name) {
                return SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                                .setCurrency("usd")
                                                                .setUnitAmount(program.getRate().multiply(new java.math.BigDecimal(100)).longValue())
                                                                .setProductData(
                                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                                                .setName(name)
                                                                                                .build()
                                                                )
                                                                .build()
                                )
                                .build();
        }

    public Session createStripeSession(Member member, BigDecimal amount) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setCustomer(member.getStripeCustomerId())
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(amount.multiply(BigDecimal.valueOf(100)).longValue())
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Monthly Subscription")
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendBaseUrl + "/signup/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(frontendBaseUrl + "/signup/cancel")
                .build();

        return Session.create(params);
    }
}
