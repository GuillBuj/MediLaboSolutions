package com.medilabosolutions.gateway.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtInterceptor implements ClientHttpRequestInterceptor {

    @Value("${JWT_TOKEN:eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1NTc2MDAzNywiZXhwIjoxNzU1ODQ2NDM3fQ.vnzjKMwPmFm0PtT0MveWQWsWkavZuPHPDq669HDgvQBpNiTcQcZ8ywFZ_DpjObs6wIlA8vCTLpraHbKFPBjEFA}")
    private String jwtToken;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add("Authorization", "Bearer " + jwtToken);
        return execution.execute(request, body);
    }
}

