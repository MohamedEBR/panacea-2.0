package com.example.panacea.controllers;

import com.example.panacea.services.SecurityService;
import com.example.panacea.services.StripeWebhookService;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StripeWebhookControllerTest {

    @Mock
    private StripeWebhookService stripeWebhookService;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private StripeWebhookController controller;

    private MockHttpServletRequest request;
    private String testPayload = "{\"id\": \"evt_test\", \"type\": \"customer.created\"}";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(controller, "endpointSecret", "test-secret");
        request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
    }

    @Test
    void testWebhookWithMissingSignature() throws Exception {
        // Given
        request.setContent(testPayload.getBytes());
        // No Stripe-Signature header set
        
        // When
        ResponseEntity<String> response = controller.handleStripeEvent(request);
        
        // Then
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Missing signature header", response.getBody());
        
        // Verify security logging was called
        verify(securityService).logSecurityEvent(
            eq("STRIPE_WEBHOOK_MISSING_SIGNATURE"), 
            eq("127.0.0.1"),
            anyString()
        );
        verifyNoInteractions(stripeWebhookService);
    }

    @Test
    void testWebhookWithEmptyPayload() throws Exception {
        // Given
        request.setContent("".getBytes());
        request.addHeader("Stripe-Signature", "test-signature");
        
        // When
        ResponseEntity<String> response = controller.handleStripeEvent(request);
        
        // Then
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Empty payload", response.getBody());
        
        // Verify security logging was called
        verify(securityService).logSecurityEvent(
            eq("STRIPE_WEBHOOK_EMPTY_PAYLOAD"), 
            eq("127.0.0.1"),
            anyString()
        );
        verifyNoInteractions(stripeWebhookService);
    }

    @Test
    void testWebhookWithInvalidSignature() throws Exception {
        // Given
        request.setContent(testPayload.getBytes());
        request.addHeader("Stripe-Signature", "invalid-signature");
        
        when(securityService.verifyStripeWebhookSignature(anyString(), anyString())).thenReturn(false);
        
        // When
        ResponseEntity<String> response = controller.handleStripeEvent(request);
        
        // Then
        assertEquals(401, response.getStatusCode().value());
        assertEquals("Invalid signature", response.getBody());
        
        // Verify interactions
        verify(securityService).verifyStripeWebhookSignature(testPayload, "invalid-signature");
        verify(securityService).logSecurityEvent(
            eq("STRIPE_WEBHOOK_INVALID_SIGNATURE"), 
            eq("127.0.0.1"),
            anyString()
        );
        verifyNoInteractions(stripeWebhookService);
    }

    @Test
    void testWebhookWithValidSignatureButInvalidEvent() throws Exception {
        // Given
        request.setContent(testPayload.getBytes());
        request.addHeader("Stripe-Signature", "valid-signature");
        
        when(securityService.verifyStripeWebhookSignature(anyString(), anyString())).thenReturn(true);
        
        // Mock Stripe Webhook.constructEvent to throw exception
        try (MockedStatic<Webhook> webhookMock = mockStatic(Webhook.class)) {
            webhookMock.when(() -> Webhook.constructEvent(anyString(), anyString(), anyString()))
                      .thenThrow(new RuntimeException("Invalid event"));
        
            // When
            ResponseEntity<String> response = controller.handleStripeEvent(request);
            
            // Then
            assertEquals(400, response.getStatusCode().value());
            assertEquals("Invalid webhook event", response.getBody());
            
            // Verify interactions
            verify(securityService).verifyStripeWebhookSignature(testPayload, "valid-signature");
            verify(securityService).logSecurityEvent(
                eq("STRIPE_WEBHOOK_CONSTRUCT_FAILED"), 
                eq("127.0.0.1"),
                anyString()
            );
            verifyNoInteractions(stripeWebhookService);
        }
    }

    @Test
    void testWebhookSuccessfulProcessing() throws Exception {
        // Given
        request.setContent(testPayload.getBytes());
        request.addHeader("Stripe-Signature", "valid-signature");
        
        when(securityService.verifyStripeWebhookSignature(anyString(), anyString())).thenReturn(true);
        
        // Mock Stripe Event
        Event mockEvent = mock(Event.class);
        when(mockEvent.getType()).thenReturn("customer.created");
        
        // Mock Stripe Webhook.constructEvent to return valid event
        try (MockedStatic<Webhook> webhookMock = mockStatic(Webhook.class)) {
            webhookMock.when(() -> Webhook.constructEvent(anyString(), anyString(), anyString()))
                      .thenReturn(mockEvent);
        
            // When
            ResponseEntity<String> response = controller.handleStripeEvent(request);
            
            // Then
            assertEquals(200, response.getStatusCode().value());
            assertEquals("Webhook handled successfully", response.getBody());
            
            // Verify security interactions
            verify(securityService).verifyStripeWebhookSignature(testPayload, "valid-signature");
            verify(securityService).logSecurityEvent(
                eq("STRIPE_WEBHOOK_RECEIVED"), 
                eq("127.0.0.1"),
                contains("customer.created")
            );
            verify(stripeWebhookService).handleStripeEvent(mockEvent);
        }
    }

    @Test
    void testWebhookIPExtractionWithXForwardedFor() throws Exception {
        // Given
        request.setContent(testPayload.getBytes());
        request.addHeader("Stripe-Signature", "valid-signature");
        request.addHeader("X-Forwarded-For", "192.168.1.100, 10.0.0.1");
        request.addHeader("X-Real-IP", "192.168.1.200");
        
        when(securityService.verifyStripeWebhookSignature(anyString(), anyString())).thenReturn(true);
        
        Event mockEvent = mock(Event.class);
        when(mockEvent.getType()).thenReturn("customer.created");
        
        try (MockedStatic<Webhook> webhookMock = mockStatic(Webhook.class)) {
            webhookMock.when(() -> Webhook.constructEvent(anyString(), anyString(), anyString()))
                      .thenReturn(mockEvent);
        
            // When
            ResponseEntity<String> response = controller.handleStripeEvent(request);
            
            // Then
            assertEquals(200, response.getStatusCode().value());
            
            // Verify that security logging includes the correct IP (first from X-Forwarded-For)
            verify(securityService).logSecurityEvent(
                eq("STRIPE_WEBHOOK_RECEIVED"), 
                eq("192.168.1.100"),
                anyString()
            );
        }
    }

    @Test
    void testWebhookProcessingException() throws Exception {
        // Given
        request.setContent(testPayload.getBytes());
        request.addHeader("Stripe-Signature", "valid-signature");
        
        when(securityService.verifyStripeWebhookSignature(anyString(), anyString())).thenReturn(true);
        
        Event mockEvent = mock(Event.class);
        when(mockEvent.getType()).thenReturn("customer.created");
        
        // Mock exception in event processing
        doThrow(new RuntimeException("Database error")).when(stripeWebhookService).handleStripeEvent(any(Event.class));
        
        try (MockedStatic<Webhook> webhookMock = mockStatic(Webhook.class)) {
            webhookMock.when(() -> Webhook.constructEvent(anyString(), anyString(), anyString()))
                      .thenReturn(mockEvent);
        
            // When
            ResponseEntity<String> response = controller.handleStripeEvent(request);
            
            // Then
            assertEquals(500, response.getStatusCode().value());
            assertEquals("Webhook processing failed", response.getBody());
            
            // Verify error handling and logging
            verify(securityService).verifyStripeWebhookSignature(testPayload, "valid-signature");
            verify(stripeWebhookService).handleStripeEvent(mockEvent);
            verify(securityService).logSecurityEvent(
                eq("STRIPE_WEBHOOK_ERROR"), 
                eq("127.0.0.1"),
                contains("Database error")
            );
        }
    }
}