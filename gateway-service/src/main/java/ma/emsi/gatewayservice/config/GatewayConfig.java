package ma.emsi.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // user-service
                .route("user-service", r -> r.path("/api/auth/**", "/api/users/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("lb://user-service"))
                // restaurant-service
                .route("restaurant-service", r -> r.path("/api/restaurants/**", "/api/menus/**", "/api/reviews/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("lb://restaurant-service"))
                // reservation-service
                .route("reservation-service", r -> r.path("/api/reservations/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("lb://reservation-service"))
                // geo-service
                .route("geo-service", r -> r.path("/api/geo/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("lb://geo-service"))
                .build();
    }
}

