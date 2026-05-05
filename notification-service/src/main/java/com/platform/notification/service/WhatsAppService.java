package com.platform.notification.service;

import com.platform.notification.config.TwilioConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WhatsAppService {
    private final TwilioConfig twilioConfig;

    @RateLimiter(name = "whatsAppMessageNotificationLimiter", fallbackMethod = "sendWhatsAppMessageRateLimited")
    @CircuitBreaker(name = "whatsAppMessageNotificationCircuitBreaker", fallbackMethod = "sendWhatsAppMessageCircuitBreakerFallback")
    @Retry(name = "whatsAppMessageNotificationRetry", fallbackMethod = "sendWhatsAppMessageRetryFallback")
    public void sendWhatsAppMessage(String phone, String message) {
//        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
//
//        Message.creator(
//                new PhoneNumber(normalizeWhatsAppNumber(phone)),
//                new PhoneNumber(normalizeWhatsAppNumber(twilioConfig.getPhone())),
//                message
//        ).create();

        log.info("Sent WhatsApp message to {}", phone);
    }

    public String handleIncomingWebhook(String from, String to, String body, String profileName, String messageSid) {
        log.info(
                "Received Twilio WhatsApp webhook: sid={}, from={}, to={}, profileName={}, body={}",
                messageSid,
                from,
                to,
                profileName,
                body
        );

        return """
                <?xml version="1.0" encoding="UTF-8"?>
                <Response>
                    <Message>Thanks for contacting Movie Ticket Booking. We received your message.</Message>
                </Response>
                """;
    }

    private String normalizeWhatsAppNumber(String phone) {
        if (phone == null || phone.isBlank()) {
            return phone;
        }
        return phone.startsWith("whatsapp:") ? phone : "whatsapp:" + phone;
    }

    @SuppressWarnings("unused")
    private void sendWhatsAppMessageRateLimited(String phone, String message, RequestNotPermitted ex) {
        log.warn("WhatsApp Message notification rate limited for phone {}", phone);
    }

    @SuppressWarnings("unused")
    private void sendWhatsAppMessageRetryFallback(String phone, String message, Exception ex) {
        log.error("WhatsApp Message notification failed after retries for phone {}", phone, ex);
    }

    @SuppressWarnings("unused")
    private void sendWhatsAppMessageCircuitBreakerFallback(String phone, String message, Exception ex) {
        log.error("WhatsApp Message notification circuit is open or failing for phone {}", phone, ex);
    }
}
