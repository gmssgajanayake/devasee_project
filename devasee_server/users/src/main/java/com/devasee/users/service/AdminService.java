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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final ModelMapper modelMapper;

    public AdminService(UserRepo userRepo, RoleRepo roleRepo, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.modelMapper = modelMapper;
    }

    // TODO : filter by role
    public List<AdminDTO> getAllAdmins() {
        try {
            return modelMapper.map(userRepo.findAllAdmins(), new TypeToken<List<AdminDTO>>() {}.getType());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch admins", e);
        }
    }

    public AdminDTO promoteAsAdmin(PromoteAsAdminDTO promoteAsAdminDTO) {
        try {

            Optional<AppUser> existingUser =  userRepo.findByEmail(promoteAsAdminDTO.getEmail());

            if(existingUser.isPresent()){
                Role adminRole = roleRepo.findByName(Roles.ADMIN.name()).orElseThrow(
                        ()-> new RuntimeException("Role Admin not found"));

                existingUser.get().getRoles().add(adminRole);
                safeSaveUser(existingUser.get());
                System.out.println("######### ADMIN role added: "+adminRole);
            }else {
                throw new RuntimeException("Before promoting ad admin you must be registered");
            }

            return modelMapper.map(existingUser, AdminDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save admin", e);
        }
    }

    public AdminDTO demoteAdmin(String email) {
        AppUser user = userRepo.findByEmail(email).orElseThrow(
                ()-> new CustomerNotFoundException("Admin not found")
        );

        Role adminRole = roleRepo.findByName(Roles.ADMIN.name()).orElseThrow(
                ()-> new RuntimeException("Role Admin not found"));

        user.getRoles().remove(adminRole);

        safeSaveUser(user);
        return modelMapper.map(user, AdminDTO.class);
    }

    public void safeSaveUser(AppUser appUser){
        try {
            userRepo.save(appUser);
        }catch (Exception e){
            System.out.println("########## error safe save user : "+e.getMessage());
            throw new RuntimeException("Failed to save user");
        }
    }

    public List<String> getUserRole(String userId) {

        System.out.println("***************** cheking2");

        Optional<AppUser> existingUser = userRepo.findById(userId);

        if (existingUser.isEmpty()) {
            System.out.println("########## User not found in database, returning default USER role");
            return new ArrayList<>(Collections.singletonList("CUSTOMER")); // Mutable list
        }

        return existingUser.get().getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList()); // This returns a mutable list
    }
}
