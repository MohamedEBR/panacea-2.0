package com.example.panacea.exceptions;

public class StripeIntegrationException extends RuntimeException {
    public StripeIntegrationException(String message, Exception e) {
        super(message);
    }
}