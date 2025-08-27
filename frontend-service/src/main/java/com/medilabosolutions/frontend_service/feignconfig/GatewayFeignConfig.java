package com.medilabosolutions.frontend_service.feignconfig;

import com.medilabosolutions.frontend_service.interceptor.JwtFeignInterceptor;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class GatewayFeignConfig {

    @Bean
    public JwtFeignInterceptor jwtFeignInterceptor() {
        log.info("*-*-*-* gatewayfeignconfig: jwtFeignInterceptor");
        return new JwtFeignInterceptor();
    }
}