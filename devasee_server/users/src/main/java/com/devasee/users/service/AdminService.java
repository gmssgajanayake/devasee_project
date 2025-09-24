package com.devasee.users.service;

import com.devasee.users.dto.AdminDTO;
import com.devasee.users.dto.PromoteDemoteAsAdminDTO;
import com.devasee.users.entity.AppUser;
import com.devasee.users.entity.Role;
import com.devasee.users.enums.AccountStatus;
import com.devasee.users.enums.Roles;
import com.devasee.users.exceptions.*;
import com.devasee.users.repository.RoleRepo;
import com.devasee.users.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public AdminService(
            UserRepo userRepo,
            RoleRepo roleRepo,
            ModelMapper modelMapper
    ) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.modelMapper = modelMapper;
    }

    public Page<AdminDTO> getAllAdmins(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
            Page<AppUser> appUserPage = userRepo.findAllByRoleName(Roles.ADMIN.name(), pageable);
            log.info("### Fetching all admins");

            return appUserPage.map(user-> {
                AdminDTO dto = modelMapper.map(user, AdminDTO.class);
                dto.setRoles(user.getRoles().stream()
                        .map(Role::getName) // only keep role names
                        .collect(Collectors.toSet())
                );
                return dto;
            });
        } catch (Exception e) {
            log.error("### Failed to fetch admins: {}", e.getMessage(), e);
            throw new ServiceUnavailableException("Failed to fetch admins : "+ e.getMessage());
        }
    }

    public AdminDTO promoteAsAdmin(PromoteDemoteAsAdminDTO promoteDemoteAsAdminDTO) {
        try {
            log.info("### Promoting user as admin: {}", promoteDemoteAsAdminDTO.getEmail());

            Optional<AppUser> existingUser =  userRepo.findByEmail(promoteDemoteAsAdminDTO.getEmail());

            log.info("*** existingUser  : {}", existingUser);

            // User must be present in database
            if(existingUser.isPresent()){

                // Get user by userId in Security contex
                String userId = SecurityContextHolder.getContext().getAuthentication() != null
                        ? SecurityContextHolder.getContext().getAuthentication().getName()
                        : null ;
                log.info("*** user ID : {}", userId);

                if(userId == null){
                    throw new InvalidAuthHeaderException("No authenticated user found in context"); // return 401
                }

                AppUser promoter = userRepo.findById(userId).orElseThrow(
                        ()-> new UserNotFoundException("Admin Not Found")
                );

                if(promoter.getRoles().stream().noneMatch(role-> role.getName().equals(Roles.ADMIN.name()))){
                    throw new UnauthorizedOperationException("non-admin trying to promote");
                }

                if(!existingUser.get().getAccountStatus().equals(AccountStatus.ACTIVE)){
                    existingUser.get().setAccountStatus(AccountStatus.ACTIVE);
                }

                Role adminRole = roleRepo.findByName(Roles.ADMIN.name()).orElseThrow(
                        ()-> new RoleNotFoundException("Role Admin not found"));

                existingUser.get().getRoles().add(adminRole);

                safeSaveUser(existingUser.get());
                log.info("### ADMIN role added to user: {}", promoteDemoteAsAdminDTO.getEmail());
            }else {
                log.warn("### Cannot promote non-registered user: {}", promoteDemoteAsAdminDTO.getEmail());
                throw new InvalidOperationException("Before promoting ad admin you must be registered");
            }

            AdminDTO dto = modelMapper.map(existingUser.get(), AdminDTO.class);
            dto.setRoles(existingUser.get().getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet()));
            return dto;
        } catch(UnauthorizedOperationException | RoleNotFoundException | InvalidOperationException e){
            log.error("### Error user promoting : {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("### Failed to save/promoting admin: {}", e.getMessage(), e);
            throw new ServiceUnavailableException("Failed to save admin : "+ e.getMessage());
        }
    }

    public AdminDTO demoteAdmin(String email) {
        try {
            log.info("### Demoting admin: {}", email);
            AppUser user = userRepo.findByEmail(email).orElseThrow(
                    () -> new UserNotFoundException("Admin not found")
            );

            Role adminRole = roleRepo.findByName(Roles.ADMIN.name()).orElseThrow(
                    () -> new RoleNotFoundException("Role Admin not found"));

            user.getRoles().remove(adminRole);

            safeSaveUser(user);
            log.info("### ADMIN role removed from user: {}", email);

            AdminDTO dto = modelMapper.map(user, AdminDTO.class);
            dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
            return dto;
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
            throw new ServiceUnavailableException("Failed to save user"+e.getMessage());
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

    public AdminDTO getAdminById(String userId) {
        try {
            AppUser admin = userRepo.findByUserIdAndRoleName(
                    userId,
                    Roles.ADMIN.name()
            ).orElseThrow(()-> new UserNotFoundException("Admin Not Found with id : "+ userId));

            AdminDTO dto = modelMapper.map(admin, AdminDTO.class);

            // Override roles to only contain names
            dto.setRoles(admin.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet()));

            return dto;
        } catch (UserNotFoundException ex){
            throw ex;
        } catch (Exception e){
            log.error("### Error fetching user by ID: {}", userId, e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later : " + e.getMessage());
        }
    }

    // Get admin name by userid
    public String getAdminNameById(String userId) {
        try {
           AppUser admin = userRepo.findByUserIdAndRoleName(userId, Roles.ADMIN.name()).orElseThrow(
                   () -> new UserNotFoundException("Admin Not Found with id : " + userId));
           return admin.getFirstName() + " " + admin.getLastName();
        } catch (UserNotFoundException ex){
            throw ex;
        } catch (Exception e){
            throw new ServiceUnavailableException("Something went wrong in server : " + e.getMessage());
        }
    }

    // Since finding someone let give roles also
    public Page<AdminDTO> searchUserByEmail(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
          Page<AppUser> usersPage = userRepo.findByEmailContainingIgnoreCase(
                  email,
                  pageable
          ).orElseThrow(()-> new UserNotFoundException("Users Not Found"));

          return usersPage.map(user-> new AdminDTO(
                  user.getUserId(),
                  user.getFirstName(),
                  user.getLastName(),
                  user.getEmail(),
                  user.getImageUrl(),
                  user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
                  user.getUpdatedAt()
          ));
        } catch (Exception e){
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later." + e.getMessage());
        }
    }

    // Return user account staus to other microservices
    public String getUserAccountStatus(String userId) {
        try{
           AppUser user = userRepo.findById(userId).orElseThrow(()-> new UserNotFoundException("Users Not Found"));
           return user.getAccountStatus().name();
        } catch (Exception e){
            throw new ServiceUnavailableException("Something went wrong in server"+e.getMessage());
        }
    }
}
