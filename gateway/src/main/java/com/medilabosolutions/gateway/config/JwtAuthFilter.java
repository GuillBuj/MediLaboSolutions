package com.medilabosolutions.gateway.config;

import com.medilabosolutions.gateway.util.JwtValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import java.util.List;

/**
 * WebFilter that processes JWT authentication from cookies.
 * Extracts and validates JWT tokens, then sets up Spring Security context for authenticated users.
 * Redirects to login page when token is invalid.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter implements WebFilter, Ordered {

    private final JwtValidator jwtValidator;

    /**
     * Filters incoming requests to check for valid JWT tokens in cookies.
     * If valid token found, sets up authentication in security context.
     * If invalid token found, redirects to login page.
     * If no token found, continues without authentication.
     *
     * @param exchange the server web exchange containing the request and response
     * @param chain the web filter chain for continuing request processing
     * @return a Mono that completes when request processing is finished
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        ServerHttpRequest req = exchange.getRequest();
        String path = req.getURI().getPath();

        String jwt = extractJwtFromCookies(req);
        if (jwt != null) {

            if (jwtValidator.isValid(jwt)) {
                log.info("Valid JWT signature for request: {}", path);

                String username = jwtValidator.extractClaim(jwt, "sub");

                var auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(new SimpleGrantedAuthority("USER"),
                                new SimpleGrantedAuthority("ADMIN")));

                var ctx = new SecurityContextImpl(auth);

                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(ctx)));
            }

            log.warn("Invalid JWT signature for request: {}", path);
            return redirect(exchange, "http://localhost:8084/login");
        }

        log.warn("No JWT token found in cookies for request: {}", path);
        return chain.filter(exchange);
    }

    /**
     * Extracts the JWT token from the request cookies.
     *
     * @param req the server HTTP request
     * @return the JWT token value if present, null otherwise
     */
    private String extractJwtFromCookies(ServerHttpRequest req) {
        return req.getCookies().getFirst("jwt") != null
                ? req.getCookies().getFirst("jwt").getValue()
                : null;
    }

    /**
     * Redirects the response to the specified URL with HTTP 303 See Other status.
     *
     * @param exchange the server web exchange
     * @param url the redirect URL
     * @return a Mono that completes when the response is finalized
     */
    private Mono<Void> redirect(ServerWebExchange exchange, String url) {
        exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
        exchange.getResponse().getHeaders().set(HttpHeaders.LOCATION, url);
        return exchange.getResponse().setComplete();
    }

    /**
     * Returns the order value of this filter (high priority).
     *
     * @return the order value (-1 indicates high priority)
     */
    @Override
    public int getOrder() {
        return -1; // priorité haute
    }
}

