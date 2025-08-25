package com.medilabosolutions.frontend_service.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

//    @Bean
//    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
//        return new BasicAuthRequestInterceptor("admin", "admin");
//    }

//    @Bean
//    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
//        return new BasicAuthRequestInterceptor("user", "password");
//    }

//    @Bean
//    public RequestInterceptor requestInterceptor() {
//        return requestTemplate -> {
//            requestTemplate.header("Accept", "application/json");
//            requestTemplate.header("Content-Type", "application/json");
//
//            // Propagation du cookie de session JSESSIONID
//            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            if (attrs != null) {
//                HttpServletRequest request = attrs.getRequest();
//                String sessionCookie = request.getHeader("Cookie");
//                if (sessionCookie != null) {
//                    requestTemplate.header("Cookie", sessionCookie);
//                }
//            }
//        };
//    }

//    @Bean
//    public RequestInterceptor feignCookieInterceptor() {
//        return requestTemplate -> {
//            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            if (attrs != null) {
//                HttpServletRequest request = attrs.getRequest();
//                String sessionId = request.getHeader("Cookie"); // récupère JSESSIONID
//                if (sessionId != null) {
//                    requestTemplate.header("Cookie", sessionId);
//                }
//            }
//            requestTemplate.header("Accept", "application/json");
//            requestTemplate.header("Content-Type", "application/json");
//        };
//    }

    @Bean
    public RequestInterceptor feignCookieInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String sessionId = request.getHeader("Cookie"); // récupère JSESSIONID
                if (sessionId != null) {
                    requestTemplate.header("Cookie", sessionId);
                }
            }
            requestTemplate.header("Accept", "application/json");
            requestTemplate.header("Content-Type", "application/json");
        };
    }

}
