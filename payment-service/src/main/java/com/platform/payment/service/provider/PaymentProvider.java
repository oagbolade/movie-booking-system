package com.platform.payment.service.provider;

public interface PaymentProvider {
    boolean processPayment(Double amount);
}