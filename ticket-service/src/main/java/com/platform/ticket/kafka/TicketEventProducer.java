package com.platform.ticket.event.producer;

import com.platform.common.event.NotificationRequestedEvent;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Retry(name = "ticketNotificationRetry", fallbackMethod = "ticketNotificationRetryFallback")
    public void sendTicketNotification(NotificationRequestedEvent event) {
        try {
            kafkaTemplate.send("notification.requested", event).join();
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish ticket notification", e);
        }
    }

    @SuppressWarnings("unused")
    private void ticketNotificationRetryFallback(NotificationRequestedEvent event, Exception ex) {
        throw new RuntimeException("Failed to publish ticket notification after retries", ex);
    }
}
