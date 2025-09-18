package com.devasee.delivery.services;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;

@Service
public class InternalJWTService {

    @Value("${security.internal-jwt.secret}")
    private String secretString;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // Decode Base64 secret and generate SecretKey
        byte[] keyBytes = Base64.getDecoder().decode(secretString);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public SecretKey getSecret() {
        return secretKey;
    }
}
