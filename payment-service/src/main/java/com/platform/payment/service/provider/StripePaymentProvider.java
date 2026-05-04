package com.platform.payment.service.provider;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;

@Component
public class StripePaymentProvider implements PaymentProvider {

    @Override
    @CircuitBreaker(name = "paymentProviderCircuitBreaker", fallbackMethod = "paymentCircuitBreakerFallback")
    @Retry(name = "paymentProviderRetry", fallbackMethod = "paymentRetryFallback")
    public boolean processPayment(Double amount) {

        // TODO: integrate real Stripe API
        System.out.println("💳 Processing payment via Stripe: " + amount);

        return true;
    }

    @SuppressWarnings("unused")
    private boolean paymentRetryFallback(Double amount, Exception ex) {
        throw new RuntimeException("Payment provider failed after retries", ex);
    }

    @SuppressWarnings("unused")
    private boolean paymentCircuitBreakerFallback(Double amount, Exception ex) {
        throw new RuntimeException("Payment provider circuit is open or failing", ex);
    }
}
