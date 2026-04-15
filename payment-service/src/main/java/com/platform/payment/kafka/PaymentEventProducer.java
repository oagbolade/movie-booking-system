package com.platform.payment.event.producer;

import com.platform.common.event.NotificationRequestedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentSuccess(NotificationRequestedEvent event) {
        kafkaTemplate.send("notification.requested", event);
    }
}