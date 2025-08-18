package com.medilabosolutions.frontend_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://gateway:8083")
                //.defaultHeaders(headers -> headers.setBasicAuth("admin", "admin"))
                .build();
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder()
                .defaultHeaders(headers -> headers.setBasicAuth("admin", "admin"));
    }
}
