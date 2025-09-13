package com.example.panacea.controllers;

import com.example.panacea.services.StripeWebhookService;
import com.example.panacea.services.SecurityService;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stripe/webhook")
@RequiredArgsConstructor
public class StripeWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookController.class);
    
    private final StripeWebhookService stripeWebhookService;
    private final SecurityService securityService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping
    public ResponseEntity<String> handleStripeEvent(HttpServletRequest request) {
        String clientIP = getClientIP(request);
        String payload = null;
        
        try {
            // Read the payload
            try (BufferedReader reader = new BufferedReader(request.getReader())) {
                payload = reader.lines().collect(Collectors.joining("\n"));
            }
            
            String sigHeader = request.getHeader("Stripe-Signature");
            
            // Validate required headers
            if (sigHeader == null || sigHeader.trim().isEmpty()) {
                logger.warn("Missing Stripe-Signature header from IP: {}", clientIP);
                securityService.logSecurityEvent("STRIPE_WEBHOOK_MISSING_SIGNATURE", clientIP, 
                    "Webhook request missing required Stripe-Signature header");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing signature header");
            }
            
            // Validate payload is not empty
            if (payload == null || payload.trim().isEmpty()) {
                logger.warn("Empty webhook payload from IP: {}", clientIP);
                securityService.logSecurityEvent("STRIPE_WEBHOOK_EMPTY_PAYLOAD", clientIP, 
                    "Webhook request with empty payload");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty payload");
            }
            
            // Additional security validation using SecurityService
            if (!securityService.verifyStripeWebhookSignature(payload, sigHeader)) {
                logger.error("Stripe webhook signature verification failed from IP: {}", clientIP);
                securityService.logSecurityEvent("STRIPE_WEBHOOK_INVALID_SIGNATURE", clientIP, 
                    "Failed signature verification for webhook");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
            }
            
            // Construct and validate the event using Stripe's library
            Event event;
            try {
                event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
                logger.info("Successfully verified Stripe webhook event: {} from IP: {}", event.getType(), clientIP);
            } catch (Exception e) {
                logger.error("Failed to construct Stripe event from IP: {}, error: {}", clientIP, e.getMessage());
                securityService.logSecurityEvent("STRIPE_WEBHOOK_CONSTRUCT_FAILED", clientIP, 
                    "Failed to construct webhook event: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid webhook event");
            }
            
            // Log successful webhook reception
            securityService.logSecurityEvent("STRIPE_WEBHOOK_RECEIVED", clientIP, 
                "Successfully received and verified webhook event: " + event.getType());
            
            // Process the event
            stripeWebhookService.handleStripeEvent(event);
            
            logger.info("Successfully processed Stripe webhook event: {} from IP: {}", event.getType(), clientIP);
            securityService.logSecurityEvent("STRIPE_WEBHOOK_PROCESSED", clientIP, 
                "Successfully processed webhook event: " + event.getType());
            
            return ResponseEntity.ok("Webhook handled successfully");
            
        } catch (Exception e) {
            logger.error("Unexpected error processing Stripe webhook from IP: {}, error: {}", clientIP, e.getMessage(), e);
            securityService.logSecurityEvent("STRIPE_WEBHOOK_ERROR", clientIP, 
                "Unexpected webhook processing error: " + e.getMessage());
            
            // Return generic error message to avoid information disclosure
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webhook processing failed");
        }
    }
    
    /**
     * Extracts the real client IP address from the request
     */
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP.trim();
        }
        
        return request.getRemoteAddr();
    }

}
