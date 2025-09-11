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

/**
 * Controller handling user authentication and login/logout operations.
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    @Value("http://localhost:8083")
    private String gatewayUrl;

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    /**
     * Displays the login form.
     *
     * @return the view name "login" to render
     */
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    /**
     * Processes login form submission and authenticates users.
     *
     * @param request the login request DTO containing username and password
     * @param model the Spring Model object to add attributes for the view
     * @param servletResponse the HTTP servlet response to add cookies
     * @param servletRequest the HTTP servlet request
     * @param redirectAttributes attributes for redirect scenarios
     * @return redirect to home page on success, or back to login page on failure
     */
    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute LoginRequestDTO request,
                              Model model,
                              HttpServletResponse servletResponse,
                              HttpServletRequest servletRequest,
                              RedirectAttributes redirectAttributes) {
        log.info("[LOGIN] loginSubmit called for user: {}", request.username());

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
            log.warn("Authentication failed for user '{}': {}", request.username(), e.getMessage());
            model.addAttribute("error", "Nom d'utilisateur ou mot de passe incorrect");
            return "login";
        }
    }

    /**
     * Handles user logout by invalidating the session.
     *
     * @param request the HTTP servlet request
     * @param response the HTTP servlet response
     * @return redirect to login page
     */
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("[LOGOUT] logout called");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}

