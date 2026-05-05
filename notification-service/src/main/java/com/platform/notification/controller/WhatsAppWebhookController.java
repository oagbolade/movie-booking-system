package com.platform.notification.controller;

import com.platform.notification.service.WhatsAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WhatsAppWebhookController {

    private final WhatsAppService whatsAppService;

    @PostMapping(path = "/whatsapp", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> receiveWebhook(@RequestBody MultiValueMap<String, String> formData) {
        String response = whatsAppService.handleIncomingWebhook(
                formData.getFirst("From"),
                formData.getFirst("To"),
                formData.getFirst("Body"),
                formData.getFirst("ProfileName"),
                formData.getFirst("MessageSid")
        );

        return ResponseEntity.ok(response);
    }
}
