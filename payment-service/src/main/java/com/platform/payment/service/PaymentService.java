package com.platform.payment.service;

import com.platform.payment.dto.*;
import com.platform.payment.model.Payment;
import com.platform.payment.repository.PaymentRepository;
import com.platform.payment.service.provider.PaymentProvider;
import com.platform.payment.event.producer.PaymentEventProducer;
import com.platform.common.event.NotificationRequestedEvent;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentProvider provider;
    private final PaymentEventProducer producer;

    public PaymentResponse process(PaymentRequest request) {

        boolean success = provider.processPayment(request.getAmount());

        Payment payment = Payment.builder()
                .userId(request.getUserId())
                .ticketId(request.getTicketId())
                .amount(request.getAmount())
                .status(success ? "SUCCESS" : "FAILED")
                .createdAt(LocalDateTime.now())
                .build();

        Payment saved = repository.save(payment);

        if (success) {
            NotificationRequestedEvent event = NotificationRequestedEvent.builder()
                    .userId(saved.getUserId())
                    .type("EMAIL")
                    .message("💰 Payment successful!")
                    .build();

            producer.sendPaymentSuccess(event);
        }

        return PaymentResponse.builder()
                .id(saved.getId())
                .status(saved.getStatus())
                .build();
    }
}