package com.platform.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(
                10, // requests per second
                20  // burst capacity
        );
    }

    // Identify user by IP (simple)
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange ->
                Mono.just(exchange.getRequest()
                        .getRemoteAddress()
                        .getAddress()
                        .getHostAddress());
    }

    // Identify user by ID
//    @Bean
//    public KeyResolver ipKeyResolver() {
//        return exchange ->
//                Mono.just(exchange.getRequest().getHeaders().getFirst("Authorization"));
//    }

    // OPTIONAL (better): by userId from JWT later
}