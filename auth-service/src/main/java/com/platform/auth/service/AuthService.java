package com.platform.auth.service;

import com.platform.auth.dto.AuthResponse;
import com.platform.auth.dto.LoginRequest;
import com.platform.auth.dto.RegisterRequest;
import com.platform.auth.exception.TooManyRequestsException;
import com.platform.common.event.NotificationRequestedEvent;
import com.platform.auth.event.producer.NotificationProducer;
import com.platform.auth.model.User;
import com.platform.auth.repository.UserRepository;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
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

    @RateLimiter(name = "registerLimiter", fallbackMethod = "registerRateLimited")
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
                .type("ALL")
                .message("Welcome to our platform 🎉")
                .build();

        notificationProducer.sendNotification(event);

        return generateTokens(user);
    }

    @RateLimiter(name = "loginLimiter", fallbackMethod = "loginRateLimited")
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        NotificationRequestedEvent event = NotificationRequestedEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .phoneNumber("+2348147471248")
                .type("ALL")
                .message("Login Successful 🎉")
                .build();

        notificationProducer.sendNotification(event);

        return generateTokens(user);
    }

    @RateLimiter(name = "refreshLimiter", fallbackMethod = "refreshRateLimited")
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

    private AuthResponse registerRateLimited(RegisterRequest request, RequestNotPermitted ex) {
        throw new TooManyRequestsException("Too many registration attempts. Please try again later.");
    }

    private AuthResponse loginRateLimited(LoginRequest request, RequestNotPermitted ex) {
        throw new TooManyRequestsException("Too many login attempts. Please try again later.");
    }

    private AuthResponse refreshRateLimited(String refreshToken, RequestNotPermitted ex) {
        throw new TooManyRequestsException("Too many token refresh attempts. Please try again later.");
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
