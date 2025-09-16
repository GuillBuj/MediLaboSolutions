package com.medilabosolutions.gateway.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Handles authentication errors by returning HTTP 401 Unauthorized status.
 * This component is triggered when a request fails JWT authentication.
 */
@Component
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    /**
     * Sets HTTP 401 status and completes the response when authentication fails.
     *
     * @param exchange the server web exchange containing the request and response
     * @param ex the authentication exception that caused the authentication failure
     * @return a Mono that completes when the HTTP 401 response is finalized
     */
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, org.springframework.security.core.AuthenticationException ex) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
