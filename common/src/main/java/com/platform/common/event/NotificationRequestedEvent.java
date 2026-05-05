package com.platform.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestedEvent {

    private String userId;
    private String email;
    private String phoneNumber;
    private String type; // EMAIL, SMS, WHATSAPP, ALL
    private String message;
}
