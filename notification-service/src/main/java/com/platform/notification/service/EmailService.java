package com.platform.notification.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendEmail(String to, String message) {

        // TODO: integrate Mailgun API
        System.out.println("📧 Sending EMAIL to " + to + ": " + message);
    }
}