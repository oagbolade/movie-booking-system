package com.platform.common.event;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestedEvent {

    private String userId;
    private String email;
    private String phoneNumber;

    private String type; // EMAIL, SMS, BOTH
    private String message;
}