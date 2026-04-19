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

// JwtUtil is the core token engine.
// It signs tokens with HMAC-SHA256 using a secret key from application.yml.
// Three responsibilities: generate, extract, validate.
// -------------------------------------------------------
@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;

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



}
