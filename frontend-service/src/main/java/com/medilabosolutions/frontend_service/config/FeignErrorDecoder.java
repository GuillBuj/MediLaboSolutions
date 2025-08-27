package com.medilabosolutions.frontend_service.config;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

//@Configuration
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("Error Response: status={}, reason={}", response.status(), response.reason());
        if (response.body() != null) {
            try {
                String responseBody = new String(response.body().asInputStream().readAllBytes());
                log.error("Detailed error response: {}", responseBody);
            } catch (IOException e) {
                log.error("Could not read response body", e);
            }
        } else {
            log.error("Response body is null");
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }
}
