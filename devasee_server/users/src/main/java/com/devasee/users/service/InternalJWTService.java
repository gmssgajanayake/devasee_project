package com.devasee.users.service;

import com.devasee.users.entity.AppUser;
import com.devasee.users.exceptions.ServiceUnavailableException;
import com.devasee.users.exceptions.UserNotFoundException;
import com.devasee.users.repository.UserRepo;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
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
           return userRepo.findById(userId).orElseThrow(()-> new UserNotFoundException("User Not Found"));
        }catch (UserNotFoundException e) {
            throw e; // propagate so we can handle it separately in the filter
        } catch (Exception e){
            throw new ServiceUnavailableException("Something went wrong in user service");
        }
    }
}
