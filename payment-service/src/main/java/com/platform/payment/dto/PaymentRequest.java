package com.platform.payment.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String userId;
    private String ticketId;
    private Double amount;
}