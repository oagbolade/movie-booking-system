package com.platform.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    @Bean
    public RedisRateLimiter authRateLimiter() {
        return new RedisRateLimiter(5, 10);
    }

    @Bean
    public RedisRateLimiter catalogueRateLimiter() {
        return new RedisRateLimiter(30, 60);
    }

    @Bean
    public RedisRateLimiter ticketRateLimiter() {
        return new RedisRateLimiter(10, 20);
    }

    @Bean
    public RedisRateLimiter paymentRateLimiter() {
        return new RedisRateLimiter(5, 10);
    }

    @Bean
    public RedisRateLimiter notificationRateLimiter() {
        return new RedisRateLimiter(3, 6);
    }

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(resolveClientIp(exchange));
    }

    @Bean
    public KeyResolver userOrIpKeyResolver() {
        return exchange -> Mono.justOrEmpty(
                        exchange.getAttribute(JwtAuthenticationFilter.AUTHENTICATED_USER_ATTR))
                .cast(String.class)
                .filter(user -> !user.isBlank())
                .map(user -> "user:" + user)
                .switchIfEmpty(Mono.fromSupplier(() -> "ip:" + resolveClientIp(exchange)));
    }

    private String resolveClientIp(org.springframework.web.server.ServerWebExchange exchange) {
        String forwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        if (exchange.getRequest().getRemoteAddress() != null
                && exchange.getRequest().getRemoteAddress().getAddress() != null) {
            return exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        }

        return "unknown";
    }
}
