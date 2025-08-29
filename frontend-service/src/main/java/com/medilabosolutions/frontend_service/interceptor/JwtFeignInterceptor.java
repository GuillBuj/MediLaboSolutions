package com.medilabosolutions.frontend_service.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.jsonwebtoken.lang.Collections;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Slf4j
public class JwtFeignInterceptor implements RequestInterceptor {

    private static final String COOKIE_NAME = "jwt";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("*-*-*-*-* JwtFeignInterceptor");
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs == null) {
            return;
        }

        HttpServletRequest request = attrs.getRequest();
        if (request == null || request.getCookies() == null)
            return;

        for (Cookie c : request.getCookies()) {
            if (COOKIE_NAME.equals(c.getName())) {

                String newCookie = COOKIE_NAME + '=' + c.getValue();

                if (requestTemplate.headers().containsKey("Cookie")) {
                    String existing = requestTemplate.headers()
                            .getOrDefault("Cookie", Collections.emptyList())
                            .stream().findFirst().orElse("");
                    requestTemplate.header("Cookie", existing + "; " + newCookie);
                } else {
                    requestTemplate.header("Cookie", newCookie);
                }
                break;
            }
        }
    }
}
