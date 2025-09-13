package com.example.panacea.services;

import com.example.panacea.dto.CreateSubscriptionRequest;
import com.example.panacea.enums.StudentStatus;
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

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StripeService {

    @Value("${stripe.api.key}")
    private String secretKey;

    private final MemberRepository memberRepository;
    private final StudentRepository studentRepository;
    private final ProgramRepository programRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public Session createStripeSession(CreateSubscriptionRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for (CreateSubscriptionRequest.StudentProgramSelection sps : request.getStudents()) {
            Student student = studentRepository.findById(sps.studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            for (Long programId : sps.programIds) {
                Program program = programRepository.findById(programId)
                        .orElseThrow(() -> new RuntimeException("Program not found"));

                // Create a line item for each program
                lineItems.add(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(program.getRate().multiply(new java.math.BigDecimal(100)).longValue()) // in cents
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Student: " + student.getName() + " - Program: " + program.getName())
                                                                .build()
                                                )
                                                .build()
                                ).build()
                );

                // Connect student to program (don't save yet — wait for webhook)
                student.getPrograms().add(program);
                student.setStatus(StudentStatus.ACTIVE);
            }
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .setCustomerEmail(member.getEmail()) // optional
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://your-frontend.com/success?session_id={CHECKOUT_SESSION_ID}") //TODO: CHECKOUT_SESSION_ID
                .setCancelUrl("https://your-frontend.com/cancel")
                .addAllLineItem(lineItems)
                .build();

        try {
            return Session.create(params);
        } catch (Exception e) {
            throw new RuntimeException("Stripe session creation failed: " + e.getMessage());
        }
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
                .setSuccessUrl("https://example.com/success")
                .setCancelUrl("https://example.com/cancel")
                .build();

        return Session.create(params);
    }
}
