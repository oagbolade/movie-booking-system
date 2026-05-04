package com.platform.payment.event.producer;

import com.platform.common.event.NotificationRequestedEvent;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @CircuitBreaker(name = "paymentNotificationCircuitBreaker", fallbackMethod = "paymentNotificationCircuitBreakerFallback")
    @Retry(name = "paymentNotificationRetry", fallbackMethod = "paymentNotificationRetryFallback")
    public void sendPaymentSuccess(NotificationRequestedEvent event) {
        try {
            kafkaTemplate.send("notification.requested", event).join();
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish payment notification", e);
        }
    }

    @SuppressWarnings("unused")
    private void paymentNotificationRetryFallback(NotificationRequestedEvent event, Exception ex) {
        throw new RuntimeException("Failed to publish payment notification after retries", ex);
    }

    @SuppressWarnings("unused")
    private void paymentNotificationCircuitBreakerFallback(NotificationRequestedEvent event, Exception ex) {
        throw new RuntimeException("Payment notification circuit is open or failing", ex);
    }
}
