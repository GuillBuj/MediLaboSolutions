package com.medilabosolutions.frontend_service.controller;

import com.medilabosolutions.frontend_service.dto.LoginRequestDTO;
import com.medilabosolutions.frontend_service.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    @Value("http://localhost:8083")
    private String gatewayUrl; // Gateway URL

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute LoginRequestDTO request,
                              Model model,
                              HttpServletResponse servletResponse,
                              HttpServletRequest servletRequest,
                              RedirectAttributes redirectAttributes) {
        log.info("[LOGIN] loginSubmit called for user: {}", request.username());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequestDTO> entity = new HttpEntity<>(request, headers);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

            String jwt = jwtUtil.generateToken(userDetails);

            Cookie jwtCookie = new Cookie("jwt", jwt);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false);
            jwtCookie.setPath("/");

            servletResponse.addCookie(jwtCookie);

            log.info("User '{}' logged in successfully", request.username());

            redirectAttributes.addFlashAttribute("success", "Connexion réussie !");
            return "redirect:/home";

        } catch (AuthenticationException e) {
            log.warn("Authentication failed for user '{}': {}",
                    request.username(), e.getMessage());
            model.addAttribute("error", "Nom d'utilisateur ou mot de passe incorrect");
            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("[LOGOUT] logout called");

        // Invalider la session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/login";
    }
}

