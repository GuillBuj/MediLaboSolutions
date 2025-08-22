package com.medilabosolutions.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtGlobalFilter implements GlobalFilter, Ordered {

    @Value("${JWT_TOKEN}")
    private String jwtToken;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getRequest()
                .mutate()
                .header("Authorization", "Bearer " + jwtToken)
                .build();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // priorité haute
    }
}

