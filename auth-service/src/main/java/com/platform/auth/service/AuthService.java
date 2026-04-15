package com.platform.auth.service;

import com.platform.auth.dto.AuthResponse;
import com.platform.auth.dto.LoginRequest;
import com.platform.auth.dto.RegisterRequest;
import com.platform.common.event.NotificationRequestedEvent;
import com.platform.auth.event.producer.NotificationProducer;
import com.platform.auth.model.User;
import com.platform.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final NotificationProducer notificationProducer;

    public AuthResponse register(RegisterRequest request) {

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of("USER"))
                .isActive(true)
                .createdAt(Instant.now())
                .build();

        userRepository.save(user);

        NotificationRequestedEvent event = NotificationRequestedEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .type("BOTH")
                .message("Welcome to our platform 🎉")
                .build();

        notificationProducer.sendNotification(event);

        return generateTokens(user);
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return generateTokens(user);
    }

    public AuthResponse refreshToken(String refreshToken) {

        User user = userRepository.findAll().stream()
                .filter(u -> refreshToken.equals(u.getRefreshToken()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (user.getRefreshTokenExpiry().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        return generateTokens(user); // rotation
    }

    private AuthResponse generateTokens(User user) {

        String accessToken = jwtService.generateToken(user);

        String newRefreshToken = UUID.randomUUID().toString();

        user.setRefreshToken(newRefreshToken);
        user.setRefreshTokenExpiry(Instant.now().plusSeconds(604800)); // 7 days

        userRepository.save(user);

        return new AuthResponse(accessToken, newRefreshToken);
    }
}