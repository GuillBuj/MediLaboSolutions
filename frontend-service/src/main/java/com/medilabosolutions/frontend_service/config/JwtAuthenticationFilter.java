package com.medilabosolutions.frontend_service.config;

import com.medilabosolutions.frontend_service.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Filter that processes JWT authentication from cookies.
 * Extracts JWT tokens from cookies and sets up Spring Security authentication.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String JWT_COOKIE_NAME = "jwt";
    private final JwtUtil jwtUtil;

    /**
     * Internal filter method that processes JWT authentication.
     *
     * @param httpServletRequest the HTTP servlet request
     * @param httpServletResponse the HTTP servlet response
     * @param filterChain the filter chain to continue processing
     * @throws ServletException if a servlet exception occurs
     * @throws IOException if an input or output exception occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String requestURI = httpServletRequest.getRequestURI();
        String jwt = extractJwtFromCookies(httpServletRequest);
        String username = null;

        if (jwt != null) {
            try {
                username = jwtUtil.extractUsername(jwt);
                log.debug("JWT found in cookie for user `{}`", username);
            } catch (Exception e) {
                log.warn("Unable to extract username from JWT(cookie): {}", e.getMessage());
            }
        } else {
            log.debug("No '{}' cookie found for request {}", JWT_COOKIE_NAME, requestURI);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(jwt)) {
                log.info("Valid JWT for `{}`", username);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(
                                new SimpleGrantedAuthority("USER"),
                                new SimpleGrantedAuthority("ADMIN")));

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                log.debug("Authentication configured for `{}`", username);
            } else {
                log.warn("Invalid or expired JWT for `{}`", username);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * Extracts JWT token from HTTP cookies.
     *
     * @param request the HTTP servlet request containing cookies
     * @return the JWT token value if found, null otherwise
     */
    private String extractJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return null;

        Optional<Cookie> jwtCookie = java.util.Arrays.stream(cookies)
                .filter(c -> JWT_COOKIE_NAME.equals(c.getName()))
                .findFirst();
        return jwtCookie.map(Cookie::getValue).orElse(null);
    }

    /**
     * Determines whether the filter should not be applied to the given request.
     * Excludes static resources and login page from JWT filtering.
     *
     * @param request the HTTP servlet request
     * @return true if the filter should not be applied, false otherwise
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/css/") ||
                path.startsWith("/actuator/") ||
                path.equals("/login");
    }
}