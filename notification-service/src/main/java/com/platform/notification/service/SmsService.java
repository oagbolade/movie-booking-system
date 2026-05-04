package com.platform.notification.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService {

    @RateLimiter(name = "smsNotificationLimiter", fallbackMethod = "sendSmsRateLimited")
    @CircuitBreaker(name = "smsNotificationCircuitBreaker", fallbackMethod = "sendSmsCircuitBreakerFallback")
    @Retry(name = "smsNotificationRetry", fallbackMethod = "sendSmsRetryFallback")
    public void sendSms(String phone, String message) {

        // TODO: integrate Twilio API
        System.out.println("📱 Sending SMS to " + phone + ": " + message);
    }

    @SuppressWarnings("unused")
    private void sendSmsRateLimited(String phone, String message, RequestNotPermitted ex) {
        log.warn("SMS notification rate limited for phone {}", phone);
    }

    @SuppressWarnings("unused")
    private void sendSmsRetryFallback(String phone, String message, Exception ex) {
        log.error("SMS notification failed after retries for phone {}", phone, ex);
    }

    @SuppressWarnings("unused")
    private void sendSmsCircuitBreakerFallback(String phone, String message, Exception ex) {
        log.error("SMS notification circuit is open or failing for phone {}", phone, ex);
    }
}
