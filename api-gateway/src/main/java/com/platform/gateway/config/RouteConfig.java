package com.platform.gateway.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    private final RedisRateLimiter authRateLimiter;
    private final RedisRateLimiter catalogueRateLimiter;
    private final RedisRateLimiter ticketRateLimiter;
    private final RedisRateLimiter paymentRateLimiter;
    private final RedisRateLimiter notificationRateLimiter;
    private final KeyResolver ipKeyResolver;
    private final KeyResolver userOrIpKeyResolver;

    public RouteConfig(@Qualifier("authRateLimiter") RedisRateLimiter authRateLimiter,
                       @Qualifier("catalogueRateLimiter") RedisRateLimiter catalogueRateLimiter,
                       @Qualifier("ticketRateLimiter") RedisRateLimiter ticketRateLimiter,
                       @Qualifier("paymentRateLimiter") RedisRateLimiter paymentRateLimiter,
                       @Qualifier("notificationRateLimiter") RedisRateLimiter notificationRateLimiter,
                       @Qualifier("ipKeyResolver") KeyResolver ipKeyResolver,
                       @Qualifier("userOrIpKeyResolver") KeyResolver userOrIpKeyResolver) {
        this.authRateLimiter = authRateLimiter;
        this.catalogueRateLimiter = catalogueRateLimiter;
        this.ticketRateLimiter = ticketRateLimiter;
        this.paymentRateLimiter = paymentRateLimiter;
        this.notificationRateLimiter = notificationRateLimiter;
        this.ipKeyResolver = ipKeyResolver;
        this.userOrIpKeyResolver = userOrIpKeyResolver;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route("auth-service", r -> r.path("/api/auth/**")
                        .filters(f -> f.requestRateLimiter(config -> config
                                .setRateLimiter(authRateLimiter)
                                .setKeyResolver(ipKeyResolver)
                        ))
                        .uri("lb://auth-service"))
                .route("catalogue-service", r -> r.path("/api/catalogue/**")
                        .filters(f -> f.requestRateLimiter(config -> config
                                .setRateLimiter(catalogueRateLimiter)
                                .setKeyResolver(userOrIpKeyResolver)
                        ))
                        .uri("lb://catalogue-service"))
                .route("ticket-service", r -> r.path("/api/tickets/**")
                        .filters(f -> f.requestRateLimiter(config -> config
                                .setRateLimiter(ticketRateLimiter)
                                .setKeyResolver(userOrIpKeyResolver)
                        ))
                        .uri("lb://ticket-service"))
                .route("payment-service", r -> r.path("/api/payments/**")
                        .filters(f -> f.requestRateLimiter(config -> config
                                .setRateLimiter(paymentRateLimiter)
                                .setKeyResolver(userOrIpKeyResolver)
                        ))
                        .uri("lb://payment-service"))
                .route("notification-service", r -> r.path("/api/notifications/**")
                        .filters(f -> f.requestRateLimiter(config -> config
                                .setRateLimiter(notificationRateLimiter)
                                .setKeyResolver(userOrIpKeyResolver)
                        ))
                        .uri("lb://notification-service"))
                .route("notification-whatsapp-webhook", r -> r.path("/whatsapp")
                        .filters(f -> f.requestRateLimiter(config -> config
                                .setRateLimiter(notificationRateLimiter)
                                .setKeyResolver(ipKeyResolver)
                        ))
                        .uri("lb://notification-service"))
                .build();
    }
}
