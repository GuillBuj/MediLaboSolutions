package com.medilabosolutions.frontend_service.feignconfig;

import com.medilabosolutions.frontend_service.interceptor.JwtFeignInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign configuration for the Gateway client.
 * Provides beans required for intercepting Feign requests,
 * mainly to add the JWT cookie to outgoing requests.
 */
@Configuration
@Slf4j
public class GatewayFeignConfig {

    /**
     * Bean providing the JWT interceptor for Feign.
     * Ensures that each outgoing Feign request contains the JWT cookie from the current HTTP context.
     *
     * @return JwtFeignInterceptor instance
     */
    @Bean
    public JwtFeignInterceptor jwtFeignInterceptor() {
        log.info("*-*-*-* gatewayfeignconfig: jwtFeignInterceptor");
        return new JwtFeignInterceptor();
    }
}