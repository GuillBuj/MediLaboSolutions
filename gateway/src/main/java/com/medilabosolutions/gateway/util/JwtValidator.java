package com.medilabosolutions.gateway.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * Component responsible for validating JWT tokens and extracting claims from them.
 * Uses HMAC-SHA algorithm with a secret key for token verification.
 */
@Component
public class JwtValidator {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Validates the integrity and authenticity of a JWT token.
     *
     * @param token the JWT token string to validate
     * @return true if the token is valid and properly signed, false otherwise
     */
    public boolean isValid(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extracts a specific claim from a JWT token.
     *
     * @param jwt the JWT token string to parse
     * @param string the name of the claim to extract
     * @return the value of the specified claim as a String
     * @throws JwtException if the token is invalid or the claim doesn't exist
     */
    public String extractClaim(String jwt, String string) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .get(string, String.class);
    }
}
