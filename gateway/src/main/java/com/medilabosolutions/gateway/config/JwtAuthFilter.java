package com.medilabosolutions.gateway.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Base64;


@Component
@Slf4j
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        log.info("[JWT FILTER] Request path: {}", path);

        // Tout passe, on ne fait plus aucune vérification
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // priorité haute
    }
}

//@Component
//@Slf4j
//public class JwtAuthFilter implements GlobalFilter, Ordered {
//    @Value("${jwt.secret}")
//    private String jwtSecret;
//
//    private static final String[] OPEN_PATHS = {"/auth/login", "/actuator/**"};
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String path = exchange.getRequest().getPath().value();
//        log.info("[JWT FILTER] Request path: {}", path);
//
//        // Autoriser les routes publiques
//        if (Arrays.stream(OPEN_PATHS).anyMatch(path::startsWith)) {
//            log.info("[JWT FILTER] Public path, no auth needed");
//            return chain.filter(exchange);
//        }
//
//        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
//        log.info("[JWT FILTER] Authorization header: {}", authHeader);
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            log.warn("[JWT FILTER] Missing or invalid Authorization header");
//            return unauthorized(exchange);
//        }
//
//        String token = authHeader.substring(7);
//
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
//                    .build()
//                    .parseClaimsJws(token);
//            log.info("[JWT FILTER] JWT is valid");
//        } catch (JwtException e) {
//            log.warn("[JWT FILTER] Invalid JWT: {}", e.getMessage());
//            return unauthorized(exchange);
//        }
//
//        return chain.filter(exchange);
//    }
//
//    private Mono<Void> unauthorized(ServerWebExchange exchange) {
//        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
//        return exchange.getResponse().setComplete();
//    }
//
//    @Override
//    public int getOrder() {
//        return -1; // priorité haute
//    }
//}