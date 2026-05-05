package com.platform.notification.controller;

import com.platform.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MailgunWebhookController {

    private final EmailService emailService;

    @PostMapping(
            path = "/mailgun/email",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    MediaType.ALL_VALUE
            },
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> receiveWebhook(
            @RequestBody(required = false) String rawBody,
            @RequestParam MultiValueMap<String, String> formData
    ) {
        emailService.handleIncomingWebhook(rawBody, formData);
        return ResponseEntity.ok(Map.of("status", "accepted"));
    }
}
