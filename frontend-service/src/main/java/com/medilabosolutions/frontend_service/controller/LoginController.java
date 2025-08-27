package com.medilabosolutions.frontend_service.controller;

import com.medilabosolutions.frontend_service.dto.LoginRequestDTO;
import com.medilabosolutions.frontend_service.dto.TokenResponse;
import com.medilabosolutions.frontend_service.interceptor.JwtFeignInterceptor;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@Slf4j
public class LoginController {

    private final String gatewayUrl = "http://localhost:8083"; // Gateway URL

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute LoginRequestDTO request,
                              Model model,
                              HttpServletResponse servletResponse,
                              HttpServletRequest servletRequest) {
        log.info("[LOGIN] loginSubmit called for user: {}", request.username());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequestDTO> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                    gatewayUrl + "/auth/login", entity, TokenResponse.class
            );

            String token = response.getBody().token();
            log.info("[LOGIN] JWT received: {}", token);

            // Stocker le token dans un cookie HttpOnly
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60); // 24h
            servletResponse.addCookie(cookie);

            // 👉 Stocker le token en session (pour Feign)
            servletRequest.getSession().setAttribute("jwt", token);

            return "redirect:/home";

        } catch (Exception e) {
            log.error("[LOGIN] Invalid credentials or error calling gateway", e);
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }

//    @PostMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        log.info("[LOGOUT] logout called");
//
//        // Supprimer le cookie JWT
//        Cookie cookie = new Cookie("jwt", null);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        cookie.setMaxAge(0); // expire immédiatement
//        response.addCookie(cookie);
//
//        // Invalider la session
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            session.invalidate();
//        }
//
//        return "redirect:/login";
//    }
}

