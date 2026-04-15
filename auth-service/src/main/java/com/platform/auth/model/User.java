package com.platform.auth.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    private String email;
    private String password;

    private List<String> roles;

    private boolean isActive;

    // Refresh token storage (rotation)
    private String refreshToken;
    private Instant refreshTokenExpiry;

    private Instant createdAt;
    private Instant updatedAt;
}