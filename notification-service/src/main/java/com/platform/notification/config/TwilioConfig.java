package com.platform.notification.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "twilio")
public class TwilioConfig {
    private String phone;
    private String accountSid;
    private String authToken;
}