package com.devasee.users.service;

import com.devasee.users.entity.AppUser;
import com.devasee.users.exceptions.ServiceUnavailableException;
import com.devasee.users.exceptions.UserNotFoundException;
import com.devasee.users.repository.UserRepo;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;

@Service
public class InternalJWTService {

    private final UserRepo userRepo;
    @Value("${security.internal-jwt.secret}")
    private String secretString;

    private SecretKey secretKey;

    private static final Logger log = LoggerFactory.getLogger(InternalJWTService.class);

    public InternalJWTService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostConstruct
    public void init() {
        // Decode Base64 secret and generate SecretKey
        byte[] keyBytes = Base64.getDecoder().decode(secretString);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public SecretKey getSecret() {
        return secretKey;
    }

    public AppUser getUserById(String userId) {
        try{
            log.info("Getting user by id (user service) : {}", userId);
           return userRepo.findById(userId).orElseThrow(()-> new UserNotFoundException("User Not Found"));
        }catch (UserNotFoundException e) {
            throw e; // propagate so we can handle it separately in the filter
        } catch (Exception e){
            log.info("Error getting user by id (user service) : {}", e.getMessage());
            throw new ServiceUnavailableException("Something went wrong in user service");
        }
    }
}
