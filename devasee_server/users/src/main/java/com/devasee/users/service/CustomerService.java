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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CustomerService {

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
            List<AppUser> appUsers = userRepo.findAll();
            return modelMapper.map(appUsers, new TypeToken<List<RetrieveUserDTO>>(){}.getType());
        } catch (Exception e) {
            throw new ServiceUnavailableException("Failed to fetch customers. Please try again later.");
        }
    }

    // Get user by id
    public RetrieveUserDTO getUserById(String id) {
        AppUser appUser = userRepo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        return modelMapper.map(appUser, RetrieveUserDTO.class);
    }

    // Save sign in users
    public CreateUserDTO saveCustomer(String authHeader) {

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
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

        System.out.println("***************** cheking1");

        if(userRepo.existsByEmail(email)) {
            System.out.println("###### User already exists : "+email);
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

        if (superAdminEmail.equalsIgnoreCase(email)) {
            Role adminRole = roleRepo.findByName(Roles.ADMIN.name()).orElseThrow(
                    ()-> new RuntimeException("Role Admin not found"));

            System.out.println("######### ADMIN role added: "+adminRole);
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
        // Token format: header.payload.signature, all base64 encoded
        String[] parts = token.split("\\.");
        if(parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT token format");
        }
        // Decode the payload (claims)
        // String payloadJson = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
        // Parse claims from payload JSON
        return Jwts.parserBuilder()
                .build()
                .parseClaimsJwt(parts[0] + "." + parts[1] + ".")  // Notice the trailing dot - no signature
                .getBody();
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
            userRepo.delete(existingUser);
        }catch (Exception e){
           System.out.println("###### deleteCustomer"+e.getMessage());
           throw new ServiceUnavailableException("Service unavailable right now");
       }
    }

    // Suspend the user
    public RetrieveUserDTO suspendUser(String userId){
        AppUser existingUser = userRepo.findById(userId).orElseThrow(
                ()-> new CustomerNotFoundException("Customer not found"));

        existingUser.setAccountStatus(AccountStatus.SUSPENDED);

        return modelMapper.map(safeSaveUser(existingUser), RetrieveUserDTO.class);
    }

    public AppUser safeSaveUser(AppUser appUser){
        try {
           return userRepo.save(appUser);
        }catch (Exception e){
            System.out.println("########## error safe save user : "+e.getMessage());
            throw new RuntimeException("Failed to save user");
        }
    }

//    public AppUser createNewUser(){
//        AppUser appUser = new AppUser();
//        appUser.setUserId(createUserDTO.getUserId());
//        appUser.setEmail(email);
//        appUser.setFirstName(firstName);
//        appUser.setLastName(lastName);
//        appUser.setImageUrl(imageUrl);
//        appUser.setUsername(username);
//        appUser.setAccountStatus(AccountStatus.ACTIVE);
//
//        Role defaultRole = roleRepo.findByName(Roles.CUSTOMER.name())
//                .orElseThrow(() -> new RuntimeException("Default role USER not found"));
//        Role adminRole = roleRepo.findByName(Roles.ADMIN.name()).orElseThrow(
//                ()-> new RuntimeException("Role Admin not found"));
//
//        appUser.getRoles().clear();  // Clear any roles to be safe
//        appUser.getRoles().add(defaultRole);
//        appUser.getRoles().add(adminRole);  // Assign only USER role
//    }
}
