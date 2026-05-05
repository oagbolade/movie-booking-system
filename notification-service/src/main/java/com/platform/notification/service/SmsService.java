package com.platform.notification.service;

import com.platform.notification.config.TwilioConfig;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.platform.notification.config.TwilioConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {
    private final TwilioConfig twilioConfig;


    @RateLimiter(name = "smsNotificationLimiter", fallbackMethod = "sendSmsRateLimited")
    @CircuitBreaker(name = "smsNotificationCircuitBreaker", fallbackMethod = "sendSmsCircuitBreakerFallback")
    @Retry(name = "smsNotificationRetry", fallbackMethod = "sendSmsRetryFallback")
    public void sendSms(String phone, String message) {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
        Message.creator(
                new PhoneNumber("+18777804236"), // to
                new PhoneNumber("+18312187277"), // from
                message).create();
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
