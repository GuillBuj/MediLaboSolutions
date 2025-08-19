package com.medilabosolutions.frontend_service.config;

import java.util.Collections;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtFeignInterceptor implements RequestInterceptor {

    private static final String COOKIE_NAME = "jwt";

    @Override
    public void apply(RequestTemplate template) {

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs == null) return;

        HttpServletRequest request = attrs.getRequest();
        if (request == null || request.getCookies() == null) return;

        for (Cookie c : request.getCookies()) {
            if (COOKIE_NAME.equals(c.getName())) {

                String newCookie = COOKIE_NAME + '=' + c.getValue();

                if (template.headers().containsKey("Cookie")) {
                    String existing = template.headers()
                            .getOrDefault("Cookie", Collections.emptyList())
                            .stream().findFirst().orElse("");
                    template.header("Cookie", existing + "; " + newCookie);
                } else {
                    template.header("Cookie", newCookie);
                }
                break;
            }
        }
    }
}