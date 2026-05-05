package com.platform.notification.service;

import com.platform.notification.config.MailgunConfig;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final MailgunConfig mailgunConfig;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @RateLimiter(name = "emailNotificationLimiter", fallbackMethod = "sendEmailRateLimited")
    @CircuitBreaker(name = "emailNotificationCircuitBreaker", fallbackMethod = "sendEmailCircuitBreakerFallback")
    @Retry(name = "emailNotificationRetry", fallbackMethod = "sendEmailRetryFallback")
    public void sendEmail(String to, String message) {
        String domain = mailgunConfig.getSandboxDomain();
        String apiKey = mailgunConfig.getApiSendingKey();
        String from = "Mailgun Sandbox <postmaster@" + domain + ">";
        String subject = "Movie Ticket Booking Notification";

        String requestBody = formField("from", from)
                + "&" + formField("to", to)
                + "&" + formField("subject", subject)
                + "&" + formField("text", message);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.mailgun.net/v3/" + domain + "/messages"))
                .header("Authorization", "Basic " + basicAuthToken(apiKey))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new RuntimeException("Mailgun request failed: " + response.statusCode() + " - " + response.body());
            }

            log.info("Sent email to {} via Mailgun", to);
        } catch (IOException | InterruptedException ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException("Failed to send email via Mailgun", ex);
        }
    }

    public void handleIncomingWebhook(String rawBody, MultiValueMap<String, String> formData) {
        log.info(
                "Received Mailgun webhook: event={}, recipient={}, subject={}, rawBody={}",
                formData.getFirst("event"),
                formData.getFirst("recipient"),
                formData.getFirst("subject"),
                rawBody
        );
    }

    private String formField(String key, String value) {
        return URLEncoder.encode(key, StandardCharsets.UTF_8)
                + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String basicAuthToken(String apiKey) {
        String credentials = "api:" + apiKey;
        return Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }

    @SuppressWarnings("unused")
    private void sendEmailRateLimited(String to, String message, RequestNotPermitted ex) {
        log.warn("Email notification rate limited for recipient {}", to);
    }

    @SuppressWarnings("unused")
    private void sendEmailRetryFallback(String to, String message, Exception ex) {
        log.error("Email notification failed after retries for recipient {}", to, ex);
    }

    @SuppressWarnings("unused")
    private void sendEmailCircuitBreakerFallback(String to, String message, Exception ex) {
        log.error("Email notification circuit is open or failing for recipient {}", to, ex);
    }
}
