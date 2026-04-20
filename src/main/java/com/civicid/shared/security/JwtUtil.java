package com.civicid.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

// This is the core JWT engine. It handles three things — generating a token when a user logs in, extracting the username from a token on subsequent requests, and validating that a token hasn't expired or been tampered with.
// JwtUtil is the core token engine.
// It signs tokens with HMAC-SHA256 using a secret key from application.yml.
// Three responsibilities: generate, extract, validate.
// -------------------------------------------------------
@Component
public class JwtUtil {
    
    // The secret key and token expiration time are injected from application.yml.
    // ----------------------------------------------
    @Value("${jwt.secret}")
    private String secret;

    // Expiration time in milliseconds (e.g., 3600000 for 1 hour).
    // -----------------------------------------------
    @Value("${jwt.expiration}")
    private long expiration;

    // Build the signing key from the secret string.
    // Keys.hmacShaKeyFor requires the secret to be at least 32 characters
    // for HS256 — enforced by the value in application.yml.
    // -------------------------------------------------------
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generate a signed JWT for a given username.
    // The token payload (claims) includes:
    //   sub  — the username (subject)
    //   iat  — issued-at timestamp
    //   exp  — expiration timestamp
    // -------------------------------------------------------
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    // Pull the username (subject) out of the token claims.
    // -------------------------------------------------------
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Check whether the token is still valid.
    // Valid means: username matches AND expiration is in the future.
    // -------------------------------------------------------
    public boolean isTokenValid(String token, String username) {
        final String extracted = extractUsername(token);
        final Date expiry = extractAllClaims(token).getExpiration();
        return extracted.equals(username) && expiry.after(new Date());
    }
}
