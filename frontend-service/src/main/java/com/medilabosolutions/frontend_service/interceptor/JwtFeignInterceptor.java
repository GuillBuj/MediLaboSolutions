package com.medilabosolutions.frontend_service.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Component
@Slf4j
public class JwtFeignInterceptor implements RequestInterceptor {

    private static final String COOKIE_NAME = "jwt";

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            log.warn("[FEIGN] No request attributes available for URL: {}", template.url());
            return;
        }

        HttpServletRequest request = attrs.getRequest();
        if (request == null || request.getCookies() == null) {
            log.warn("[FEIGN] No cookies found for URL: {}", template.url());
            return;
        }

        Arrays.stream(request.getCookies())
                .filter(c -> COOKIE_NAME.equals(c.getName()))
                .findFirst()
                .ifPresentOrElse(
                        c -> {
                            template.header("Authorization", "Bearer " + c.getValue());
                            log.info("[FEIGN] JWT added for request to URL {}: {}", template.url(), c.getValue());
                        },
                        () -> log.warn("[FEIGN] JWT cookie not found for URL: {}", template.url())
                );
    }
}
