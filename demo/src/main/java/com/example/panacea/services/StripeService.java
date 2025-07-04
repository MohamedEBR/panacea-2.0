package com.example.panacea.services;

import com.example.panacea.exceptions.StripeIntegrationException;
import com.example.panacea.models.Member;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceItem;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.InvoiceCreateParams;
import com.stripe.param.InvoiceItemCreateParams;
import com.stripe.param.SubscriptionListParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StripeService {

    public StripeService(@Value("${stripe.api.key}") String apiKey) {
        Stripe.apiKey = apiKey;
    }

    /**
     * Create a Stripe Customer for a new member
     */
    public String createCustomer(Member member) {
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(member.getEmail())
                .setName(member.getName() + " " + member.getLastName())
                .setPhone(member.getPhone())
                .build();
        try {
            Customer customer = Customer.create(params);
            return customer.getId();
        } catch (StripeException e) {
            throw new StripeIntegrationException("Stripe customer creation failed: " + e.getMessage(), e);
        }
    }

    /**
     * Attach and set default a PaymentMethod to a customer
     */
    public void attachPaymentMethodToCustomer(String customerId, String paymentMethodId) {
        try {
            PaymentMethod pm = PaymentMethod.retrieve(paymentMethodId);
            Map<String, Object> attachParams = new HashMap<>();
            attachParams.put("customer", customerId);
            pm.attach(attachParams);

            Map<String, Object> updateParams = new HashMap<>();
            updateParams.put("invoice_settings", Map.of("default_payment_method", paymentMethodId));
            Customer customer = Customer.retrieve(customerId);
            customer.update(updateParams);
        } catch (StripeException e) {
            throw new StripeIntegrationException("Failed to attach payment method: " + e.getMessage(), e);
        }
    }

    /**
     * Create a line item for an upcoming invoice
     */
    public void createInvoiceItem(String customerId, BigDecimal amount, String description) {
        try {
            long cents = amount.multiply(BigDecimal.valueOf(100)).longValue();
            InvoiceItemCreateParams params = InvoiceItemCreateParams.builder()
                    .setCustomer(customerId)
                    .setAmount(cents)
                    .setCurrency("usd")
                    .setDescription(description)
                    .build();
            InvoiceItem.create(params);
        } catch (StripeException e) {
            throw new StripeIntegrationException("Failed to create invoice item: " + e.getMessage(), e);
        }
    }

    /**
     * Finalize and send the invoice for payment
     */
    public String createInvoice(String customerId) {
        try {
            InvoiceCreateParams params = InvoiceCreateParams.builder()
                    .setCustomer(customerId)
                    .setAutoAdvance(true)
                    .build();
            Invoice invoice = Invoice.create(params);
            return invoice.getId();
        } catch (StripeException e) {
            throw new StripeIntegrationException("Failed to create invoice: " + e.getMessage(), e);
        }
    }

    /**
     * Cancel all active subscriptions for a customer
     */
    public void cancelCustomerSubscription(String customerId) {
        try {
            SubscriptionListParams params = SubscriptionListParams.builder()
                    .setCustomer(customerId)
                    .setStatus(SubscriptionListParams.Status.ACTIVE)
                    .build();
            for (Subscription sub : Subscription.list(params).getData()) {
                sub.cancel();
            }
        } catch (StripeException e) {
            throw new StripeIntegrationException("Failed to cancel subscription: " + e.getMessage(), e);
        }
    }
}
