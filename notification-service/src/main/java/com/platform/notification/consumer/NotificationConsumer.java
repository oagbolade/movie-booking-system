package com.platform.notification.consumer;

import com.platform.common.event.NotificationRequestedEvent;
import com.platform.notification.service.EmailService;
import com.platform.notification.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final EmailService emailService;
    private final SmsService smsService;

    @KafkaListener(topics = "notification.requested", groupId = "notification-service")
    public void consume(NotificationRequestedEvent event) {
        try {
            log.info("Received notification event for user: {}", event.getUserId());

            if ("EMAIL".equals(event.getType()) || "BOTH".equals(event.getType())) {
                emailService.sendEmail(event.getEmail(), event.getMessage());
            }

            if ("SMS".equals(event.getType()) || "BOTH".equals(event.getType())) {
                smsService.sendSms(event.getPhoneNumber(), event.getMessage());
            }

            log.info("Notification sent successfully for user: {}", event.getUserId());
        } catch (Exception e) {
            log.error("Failed to process notification for user {}: {}", event.getUserId(), e.getMessage(), e);
            // You can implement retry logic or dead letter queue handling here if needed
        }
    }
}