package com.devasee.users.service;

import com.devasee.users.dto.CreateUserDTO;
import com.devasee.users.dto.RetrieveUserDTO;
import com.devasee.users.entity.Role;
import com.devasee.users.enums.AccountStatus;
import com.devasee.users.entity.AppUser;
import com.devasee.users.enums.Roles;
import com.devasee.users.exceptions.CustomerAlreadyExistsException;
import com.devasee.users.exceptions.CustomerNotFoundException;
import com.devasee.users.exceptions.InvalidAuthHeaderException;
import com.devasee.users.exceptions.ServiceUnavailableException;
import com.devasee.users.repository.RoleRepo;
import com.devasee.users.repository.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);


    @Value("${app.super-admin.email}")
    private String superAdminEmail;

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final RoleRepo roleRepo;

    public CustomerService(UserRepo userRepo, ModelMapper modelMapper, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.roleRepo = roleRepo;
    }

    // Get call users/customers
    public List<RetrieveUserDTO> getAllUsers() {
        try {
            log.info("### Fetching all users/customers");
            List<AppUser> appUsers = userRepo.findAll();
            return modelMapper.map(appUsers, new TypeToken<List<RetrieveUserDTO>>(){}.getType());
        } catch (Exception e) {
            log.error("### Failed to fetch customers: {}", e.getMessage(), e);
            throw new ServiceUnavailableException("Failed to fetch customers. Please try again later.");
        }
    }

    // Get user by id
    public RetrieveUserDTO getUserById(String id) {
        log.info("### Fetching customer by ID: {}", id);
        AppUser appUser = userRepo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        return modelMapper.map(appUser, RetrieveUserDTO.class);
    }

    // Save sign in users
    public CreateUserDTO saveCustomer(String authHeader) {

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            log.warn("### Missing or invalid Authorization header");
            throw new InvalidAuthHeaderException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix

        Claims claims = parseJwtWithoutValidation(token);

        String customerId = claims.get("userId", String.class);
        String email = claims.get("email", String.class);
        String firstName = claims.get("firstName", String.class);
        String lastName = claims.get("lastName", String.class);
        String username = claims.get("username", String.class);
        String imageUrl = claims.get("imageUrl", String.class);

        log.info("### Creating new customer: {} ({})", email, customerId);

        if(userRepo.existsByEmail(email)) {
            log.warn("### Customer already exists: {}", email);
            throw new CustomerAlreadyExistsException("Customer with email '" + email + "' already exists");
        }

        AppUser appUser = new AppUser();
        appUser.setUserId(customerId);
        appUser.setEmail(email);
        appUser.setFirstName(firstName);
        appUser.setLastName(lastName);
        appUser.setImageUrl(imageUrl);
        appUser.setUsername(username);
        appUser.setAccountStatus(AccountStatus.ACTIVE);

        // Adding default role as ROLE_USER
        Role defaultRole = roleRepo.findByName(Roles.CUSTOMER.name())
                .orElseThrow(() -> new RuntimeException("Default role USER not found"));

        appUser.getRoles().clear();  // Clear any roles to be safe
        appUser.getRoles().add(defaultRole);  // Assign only USER role

        if (
                superAdminEmail.equalsIgnoreCase(email) ||
                "deshithacscp@gmail.com".equalsIgnoreCase(email) ||
                "2021sp026@univ.jfn.ac.lk".equalsIgnoreCase(email) ||
                "2021sp053@univ.jfn.ac.lk".equalsIgnoreCase(email) ||
                "Gaganatday@gmail.com".equalsIgnoreCase(email)

        ) {
            Role adminRole = roleRepo.findByName(Roles.ADMIN.name()).orElseThrow(
                    ()-> new RuntimeException("Role Admin not found"));

            log.info("### ADMIN role added to customer: {} | Role: {}", email, adminRole);
            appUser.getRoles().add(adminRole);  // Assign only USER role
        }


//        if ( // TODO : move to login
//            customer.getAccountStatus().equals(AccountStatus.SUSPENDED)||
//            customer.getAccountStatus().equals(AccountStatus.BANNED)
//        ) throw new RuntimeException("Account is banned");

        safeSaveUser(appUser);
        return modelMapper.map(appUser, CreateUserDTO.class);
    }

    // Extract Claim from the token
    public Claims parseJwtWithoutValidation(String token) {
        try {
            // Token format: header.payload.signature, all base64 encoded
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT token format");
            }
            // Decode the payload (claims)
            // String payloadJson = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
            // Parse claims from payload JSON
            return Jwts.parserBuilder()
                    .build()
                    .parseClaimsJwt(parts[0] + "." + parts[1] + ".")  // Notice the trailing dot - no signature
                    .getBody();
        } catch (Exception e){
            log.error("### Failed to parse JWT token: {}", e.getMessage(), e);
            throw e;
        }
    }

    //    public RetrieveUserDTO updateCustomer(RetrieveUserDTO dto) {
    //
    //        AppUser existing = userRepo.findById(dto.getUserId())
    //                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + dto.getUserId()));
    //
    //        AppUser updatedAppUser = modelMapper.map(dto, AppUser.class);
    //        updatedAppUser.setUserId(existing.getUserId());
    //        AppUser saved = userRepo.save(updatedAppUser);
    //        return modelMapper.map(saved, RetrieveUserDTO.class);
    //    }

    // Delete user by id
    public void deleteUser(String userId) {
        AppUser existingUser = userRepo.findById(userId).orElseThrow(()->
                new CustomerNotFoundException("Customer not found with id: " + userId)
        );

       try {
           log.info("### Deleted customer with ID: {}", userId);
           userRepo.delete(existingUser);
        }catch (Exception e){
           log.error("### Error deleting customer {}: {}", userId, e.getMessage(), e);
           throw new ServiceUnavailableException("Service unavailable right now");
       }
    }

    // Suspend the user
    public RetrieveUserDTO suspendUser(String userId){
        AppUser existingUser = userRepo.findById(userId).orElseThrow(
                ()-> new CustomerNotFoundException("Customer not found"));

        existingUser.setAccountStatus(AccountStatus.SUSPENDED);
        log.info("### Suspended customer with ID: {}", userId);
        return modelMapper.map(safeSaveUser(existingUser), RetrieveUserDTO.class);
    }

    public AppUser safeSaveUser(AppUser appUser){
        try {
            AppUser saved = userRepo.save(appUser);
            log.info("### User saved: {} | Roles: {}", appUser.getEmail(),
                    appUser.getRoles().stream().map(Role::getName).toList());
            return saved;
        }catch (Exception e){
            log.error("### Error safe saving user {}: {}", appUser.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Failed to save user");
        }
    }
}
