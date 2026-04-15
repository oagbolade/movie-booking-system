package com.platform.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    private final RedisRateLimiter redisRateLimiter;
    private final KeyResolver ipKeyResolver;

    public RouteConfig(RedisRateLimiter redisRateLimiter,
                       KeyResolver ipKeyResolver) {
        this.redisRateLimiter = redisRateLimiter;
        this.ipKeyResolver = ipKeyResolver;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("lb://auth-service"))
                .route("catalogue-service", r -> r.path("/api/catalogue/**")
//                        .filters(f -> f.requestRateLimiter(config -> config
//                                .setRateLimiter(redisRateLimiter)
//                                .setKeyResolver(ipKeyResolver)
//                        ))
                        .uri("lb://catalogue-service"))
                .route("ticket-service", r -> r.path("/api/tickets/**")
                        .uri("lb://ticket-service"))
                .route("payment-service", r -> r.path("/api/payments/**")
                        .uri("lb://payment-service"))
                .route("notification-service", r -> r.path("/api/notifications/**")
                        .uri("lb://notification-service"))
                .build();
    }
}