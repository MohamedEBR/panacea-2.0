package com.example.panacea.controllers;

import com.example.panacea.dto.CreateSubscriptionRequest;
import com.example.panacea.services.StripeService;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripeController {

    private final StripeService stripeService;

    @PostMapping("/create-subscription")
    public ResponseEntity<String> createSubscription(@RequestBody CreateSubscriptionRequest request) {
        Session session = stripeService.createStripeSession(request);
        return ResponseEntity.ok(session.getUrl()); // redirect user to Stripe checkout
    }
}
