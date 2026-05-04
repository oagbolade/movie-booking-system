package com.platform.notification.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @RateLimiter(name = "emailNotificationLimiter", fallbackMethod = "sendEmailRateLimited")
    @CircuitBreaker(name = "emailNotificationCircuitBreaker", fallbackMethod = "sendEmailCircuitBreakerFallback")
    @Retry(name = "emailNotificationRetry", fallbackMethod = "sendEmailRetryFallback")
    public void sendEmail(String to, String message) {

        // TODO: integrate Mailgun API
        System.out.println("📧 Sending EMAIL to " + to + ": " + message);
    }

    @SuppressWarnings("unused")
    private void sendEmailRateLimited(String to, String message, RequestNotPermitted ex) {
        log.warn("Email notification rate limited for recipient {}", to);
    }

    @SuppressWarnings("unused")
    private void sendEmailRetryFallback(String to, String message, Exception ex) {
        log.error("Email notification failed after retries for recipient {}", to, ex);
    }

    @SuppressWarnings("unused")
    private void sendEmailCircuitBreakerFallback(String to, String message, Exception ex) {
        log.error("Email notification circuit is open or failing for recipient {}", to, ex);
    }
}
