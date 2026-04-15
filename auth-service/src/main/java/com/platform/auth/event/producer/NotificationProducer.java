package com.platform.auth.event.producer;

import com.platform.common.event.NotificationRequestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

    private final KafkaTemplate<String, NotificationRequestedEvent> kafkaTemplate;

    public void sendNotification(NotificationRequestedEvent event) {
        try {
            kafkaTemplate.send("notification.requested", event.getUserId(), event);
            log.info("Notification event sent successfully for user: {}", event.getUserId());
        } catch (Exception e) {
            log.error("Failed to send notification event for user {}: {}", event.getUserId(), e.getMessage(), e);
            throw new RuntimeException("Failed to send notification", e);
        }
    }
}