package com.platform.notification.service;

import org.springframework.stereotype.Service;

@Service
public class SmsService {

    public void sendSms(String phone, String message) {

        // TODO: integrate Twilio API
        System.out.println("📱 Sending SMS to " + phone + ": " + message);
    }
}