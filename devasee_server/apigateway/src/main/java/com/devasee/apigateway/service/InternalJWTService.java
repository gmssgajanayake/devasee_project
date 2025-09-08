package com.devasee.apigateway.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class InternalJWTService {

    private final SecretKey secret;

    public InternalJWTService(@Value("${security.internal-jwt.secret}") String secretString) {
        this.secret = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    public String generateInternalToken(String userId, List<String> roles) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userId)
                .claim("roles", roles)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(5, ChronoUnit.MINUTES))) // short-lived
                .signWith(secret)
                .compact();
    }

    public SecretKey getSecret() {
        return secret;
    }

}
