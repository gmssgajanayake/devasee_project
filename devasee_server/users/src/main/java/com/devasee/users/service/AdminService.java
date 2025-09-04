package com.devasee.users.service;

import com.devasee.users.dto.AdminDTO;
import com.devasee.users.dto.PromoteAsAdminDTO;
import com.devasee.users.entity.AppUser;
import com.devasee.users.entity.Role;
import com.devasee.users.enums.Roles;
import com.devasee.users.exceptions.CustomerNotFoundException;
import com.devasee.users.repository.RoleRepo;
import com.devasee.users.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final ModelMapper modelMapper;

    public AdminService(UserRepo userRepo, RoleRepo roleRepo, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.modelMapper = modelMapper;
    }

    public List<AdminDTO> getAllAdmins() {
        try {
            log.info("### Fetching all admins");
            return modelMapper.map(userRepo.findAllAdmins(), new TypeToken<List<AdminDTO>>() {}.getType());
        } catch (Exception e) {
            log.error("### Failed to fetch admins: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch admins", e);
        }
    }

    public AdminDTO promoteAsAdmin(PromoteAsAdminDTO promoteAsAdminDTO) {
        try {
            log.info("### Promoting user as admin: {}", promoteAsAdminDTO.getEmail());

            Optional<AppUser> existingUser =  userRepo.findByEmail(promoteAsAdminDTO.getEmail());

            if(existingUser.isPresent()){
                Role adminRole = roleRepo.findByName(Roles.ADMIN.name()).orElseThrow(
                        ()-> new RuntimeException("Role Admin not found"));

                existingUser.get().getRoles().add(adminRole);
                safeSaveUser(existingUser.get());
                log.info("### ADMIN role added to user: {}", promoteAsAdminDTO.getEmail());
            }else {
                log.warn("### Cannot promote non-registered user: {}", promoteAsAdminDTO.getEmail());
                throw new RuntimeException("Before promoting ad admin you must be registered");
            }

            return modelMapper.map(existingUser, AdminDTO.class);
        } catch (Exception e) {
            log.error("### Failed to save admin: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save admin", e);
        }
    }

    public AdminDTO demoteAdmin(String email) {
        try {
            log.info("### Demoting admin: {}", email);
            AppUser user = userRepo.findByEmail(email).orElseThrow(
                    () -> new CustomerNotFoundException("Admin not found")
            );

            Role adminRole = roleRepo.findByName(Roles.ADMIN.name()).orElseThrow(
                    () -> new RuntimeException("Role Admin not found"));

            user.getRoles().remove(adminRole);

            safeSaveUser(user);
            log.info("### ADMIN role removed from user: {}", email);
            return modelMapper.map(user, AdminDTO.class);
        } catch (Exception e){
            log.error("### Failed to demote admin {}: {}", email, e.getMessage(), e);
            throw e;
        }
    }

    public void safeSaveUser(AppUser appUser){
        try {
            userRepo.save(appUser);
            log.info("### User saved: {} | Roles: {}", appUser.getEmail(),
                    appUser.getRoles().stream().map(Role::getName).toList());
        }catch (Exception e){
            log.error("### Error safe saving user {}: {}", appUser.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Failed to save user");
        }
    }

    public List<String> getUserRole(String userId) {
        try {
            Optional<AppUser> existingUser = userRepo.findById(userId);

            if (existingUser.isEmpty()) {
                log.warn("### User not found in database, returning default CUSTOMER role");
                return new ArrayList<>(Collections.singletonList(Roles.CUSTOMER.name())); // Mutable list
            }

            List<String> roles = existingUser.get().getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toList()); // This returns a mutable list

            log.info("### Roles for user {}: {}", userId, roles);
            return roles.isEmpty()
                    ? new ArrayList<>(Collections.singletonList(Roles.CUSTOMER.name()))
                    : roles;
        } catch (Exception e) {
            log.error("### Failed to get roles for userId {}: {}", userId, e.getMessage(), e);
            return new ArrayList<>(Collections.singletonList(Roles.CUSTOMER.name())); // Mutable list
        }
    }
}
