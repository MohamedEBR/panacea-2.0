package com.example.panacea.services;

import com.example.panacea.exceptions.StripeIntegrationException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import com.stripe.param.SubscriptionListParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    public StripeService(@Value("${stripe.api.key}") String apiKey) {
        Stripe.apiKey = apiKey;
    }

    public String createCustomer(String name, String email) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);

        Customer customer = Customer.create(params);
        return customer.getId();
    }

    public void attachPaymentMethodToCustomer(String stripeCustomerId, String paymentMethodId) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("customer", stripeCustomerId);

        com.stripe.model.PaymentMethod paymentMethod = com.stripe.model.PaymentMethod.retrieve(paymentMethodId);
        paymentMethod.attach(params);

        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("invoice_settings", Map.of("default_payment_method", paymentMethodId));

        Customer customer = Customer.retrieve(stripeCustomerId);
        customer.update(customerParams);
    }


    public void cancelCustomerSubscription(String customerId) throws StripeException {
        SubscriptionListParams params = SubscriptionListParams.builder()
                .setCustomer(customerId)
                .setStatus(SubscriptionListParams.Status.ACTIVE)
                .build();

        Subscription.list(params).getData().forEach(subscription -> {
            try {
                subscription.cancel();
            } catch (StripeException e) {
                throw new StripeIntegrationException("Failed to cancel subscription: " + subscription.getId() + ". Error: " + e.getMessage());
            }
        });
    }
}