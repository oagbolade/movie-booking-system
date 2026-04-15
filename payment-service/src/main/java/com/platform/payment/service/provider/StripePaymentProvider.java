package com.platform.payment.service.provider;

import org.springframework.stereotype.Component;

@Component
public class StripePaymentProvider implements PaymentProvider {

    @Override
    public boolean processPayment(Double amount) {

        // TODO: integrate real Stripe API
        System.out.println("💳 Processing payment via Stripe: " + amount);

        return true;
    }
}