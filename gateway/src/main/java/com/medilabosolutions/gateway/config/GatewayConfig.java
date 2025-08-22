package com.medilabosolutions.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

//TODO: voir si utile

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("patient-service", r -> r.path("/api/patients/**").uri("lb://patient-service"))
                .route("note-service", r -> r.path("/api/notes/**").uri("lb://note-service"))
                .route("report-service", r -> r.path("/api/report/**").uri("lb://report-service"))
                .build();
    }
}
