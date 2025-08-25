package com.medilabosolutions.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/login", "/css/**", "/js/**").permitAll()
                        .anyExchange().authenticated()
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // simplification
                .build();
    }

//    @Bean
//    public MapReactiveUserDetailsService userDetailsService(PasswordEncoder encoder) {
//        UserDetails user = User.withUsername("user")
//                .password(encoder.encode("password"))
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.withUsername("admin")
//                .password(encoder.encode("admin"))
//                .roles("ADMIN")
//                .build();
//
//        return new MapReactiveUserDetailsService(user, admin);
//    }
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        return http
//                .csrf(csrf -> csrf.disable())
//                .authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
//                        .pathMatchers("/api/**").authenticated()
//                        .anyExchange().permitAll()
//                )
//                .formLogin(form -> form
//                        .loginPage("/login") // page HTML existante
//                        .authenticationSuccessHandler((webFilterExchange, authentication) ->
//                                Mono.fromRunnable(() ->
//                                        webFilterExchange.getExchange()
//                                                .getResponse()
//                                                .setStatusCode(HttpStatus.FOUND)
//                                )
//                        )
//                        .authenticationFailureHandler((webFilterExchange, exception) ->
//                                Mono.fromRunnable(() ->
//                                        webFilterExchange.getExchange()
//                                                .getResponse()
//                                                .setStatusCode(HttpStatus.UNAUTHORIZED)
//                                )
//                        )
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessHandler((webFilterExchange, authentication) ->
//                                Mono.fromRunnable(() ->
//                                        webFilterExchange.getExchange()
//                                                .getResponse()
//                                                .setStatusCode(HttpStatus.FOUND)
//                                )
//                        )
//                )
//                .build();
//    }
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}


