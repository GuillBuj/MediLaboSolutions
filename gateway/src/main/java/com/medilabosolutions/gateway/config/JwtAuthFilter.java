package com.medilabosolutions.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.Base64;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private static final String[] SECURED_PATHS = {"/api/patients", "/api/notes", "/api/report"};
    private static final String BASIC_AUTH_PREFIX = "Basic ";
    private static final String BEARER_AUTH_PREFIX = "Bearer ";

    // Credentials en dur dans le code
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "admin";
    private static final String VALID_BASIC_AUTH = "Basic " +
            Base64.getEncoder().encodeToString((VALID_USERNAME + ":" + VALID_PASSWORD).getBytes());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // Vérifier si le chemin nécessite une authentification
        if (requiresAuthentication(path)) {
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (authHeader == null) {
                return unauthorizedResponse(exchange, "Authorization header required");
            }

            // Support à la fois Basic et Bearer auth
            if (authHeader.startsWith(BASIC_AUTH_PREFIX)) {
                // Validation Basic Auth
                if (!isValidBasicAuth(authHeader)) {
                    return unauthorizedResponse(exchange, "Invalid basic credentials");
                }
            } else if (authHeader.startsWith(BEARER_AUTH_PREFIX)) {
                // Validation JWT
                String token = authHeader.substring(BEARER_AUTH_PREFIX.length());
                if (!isValidJwt(token)) {
                    return unauthorizedResponse(exchange, "Invalid or expired JWT token");
                }
            } else {
                return unauthorizedResponse(exchange, "Unsupported authentication type");
            }
        }

        return chain.filter(exchange);
    }

    private boolean requiresAuthentication(String path) {
        for (String securedPath : SECURED_PATHS) {
            if (path.startsWith(securedPath)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidBasicAuth(String authHeader) {
        try {
            // Compare directement avec la valeur Basic auth valide
            return VALID_BASIC_AUTH.equals(authHeader);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidJwt(String token) {
        try {
            // Validation très basique du JWT
            String[] parts = token.split("\\.");
            return parts.length == 3; // Un JWT valide a 3 parties
        } catch (Exception e) {
            return false;
        }
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String responseBody = "{\"error\": \"" + message + "\"}";

        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory()
                        .wrap(responseBody.getBytes()))
        );
    }

    @Override
    public int getOrder() {
        return -1; // Exécuter ce filtre en premier
    }
}