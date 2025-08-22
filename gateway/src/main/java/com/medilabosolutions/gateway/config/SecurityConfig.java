package com.medilabosolutions.gateway.config;

//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.beans.factory.annotation.Value;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import javax.crypto.SecretKey;
//import java.util.Date;
//
//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig {
//
//    @Value("${jwt.secret}")
//    private String jwtSecret;
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        return http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("/auth/login").permitAll()
//                        .anyExchange().authenticated()
//                )
//                .addFilterAt(jwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
//                .build();
//    }
//
//    private WebFilter jwtAuthenticationFilter() {
//        return (exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            String path = request.getPath().toString();
//
//            if (path.startsWith("/auth/login")) {
//                return chain.filter(exchange);
//            }
//
//            String token;
//            try {
//                token = extractToken(request);
//                Jwts.parserBuilder()
//                        .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
//                        .build()
//                        .parseClaimsJws(token);
//
//                // si on veut, on pourrait mettre l'utilisateur dans le SecurityContext
//                return chain.filter(exchange);
//
//            } catch (Exception e) {
//                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                exchange.getResponse().getHeaders().add("Content-Type", "application/json");
//                byte[] bytes = "{\"error\":\"Unauthorized or invalid token\"}".getBytes();
//                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
//                return exchange.getResponse().writeWith(Mono.just(buffer));
//            }
//        };
//    }
//
//    private String extractToken(ServerHttpRequest request) {
//        String authHeader = request.getHeaders().getFirst("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            return authHeader.substring(7);
//        }
//        throw new RuntimeException("Missing or invalid Authorization header");
//    }
//}

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll()
                )
                .build();
    }
}


