package com.medilabosolutions.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        return http
                // Désactive CSRF car on utilise JWT et pas de session
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // Gère les autorisations
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/auth/login").permitAll()   // Login public
                        .pathMatchers("/actuator/**").permitAll()  // Optionnel
                        .anyExchange().authenticated()             // Tout le reste sécurisé
                )

                // Pas de formLogin ni httpBasic
                .build();
    }
}
