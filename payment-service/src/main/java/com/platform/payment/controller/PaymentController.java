package com.platform.payment.controller;

import com.platform.payment.dto.*;
import com.platform.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    public PaymentResponse pay(@RequestBody PaymentRequest request) {
        return service.process(request);
    }
}