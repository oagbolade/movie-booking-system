package com.platform.ticket.event.producer;

import com.platform.common.event.NotificationRequestedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendTicketNotification(NotificationRequestedEvent event) {
        kafkaTemplate.send("notification.requested", event);
    }
}