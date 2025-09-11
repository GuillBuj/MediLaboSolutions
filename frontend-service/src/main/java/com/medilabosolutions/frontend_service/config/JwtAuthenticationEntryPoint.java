package com.medilabosolutions.frontend_service.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Component that handles authentication entry point for JWT security.
 * This class is called when an unauthenticated user tries to access a secured resource.
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Commences an authentication scheme.
     * Redirects to login page for web requests, returns 401 for API requests.
     *
     * @param request the request that caused the AuthenticationException
     * @param response the response to send the authentication challenge
     * @param authException the exception that caused the invocation
     * @throws IOException if an input or output exception occurs
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        log.warn("Unauthorized access to {} {} - {}", method, requestURI, authException.getMessage());

        if (isApiRequest(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            response.sendRedirect("/login");
        }
    }

    /**
     * Determines if the request is an API request based on the URI.
     *
     * @param request the HTTP servlet request
     * @return true if the request URI starts with "/api/", false otherwise
     */
    private boolean isApiRequest(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith("/api/");
    }
}
