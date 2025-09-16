package com.medilabosolutions.gateway.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Controller handling authentication operations including user login and JWT token generation.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Authenticates user credentials and generates a JWT token upon successful login.
     *
     * @param request the login request containing username and password
     * @return ResponseEntity with JWT token if authentication succeeds,
     *         or 401 Unauthorized with error message if authentication fails
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if ("admin".equals(request.getUsername()) && "admin".equals(request.getPassword())) {
            String token = generateToken(request.getUsername());
            return ResponseEntity.ok(new TokenResponse(token));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    /**
     * Generates a JWT token for the authenticated user.
     *
     * @param username the username to include as subject in the token
     * @return the generated JWT token string
     */
    private String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }
}

/**
 * Data transfer object for login requests.
 */
@Data
class LoginRequest {
    private String username;
    private String password;
}

/**
 * Data transfer object for token responses.
 */
@Data
@AllArgsConstructor
class TokenResponse {
    private String token;
}
