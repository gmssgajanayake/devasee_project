package com.devasee.users.init;

import com.devasee.users.entity.Role;
import com.devasee.users.enums.Roles;
import com.devasee.users.repository.RoleRepo;
import com.devasee.users.repository.UserRepo;
import com.devasee.users.service.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepo roleRepo;


    public DataInitializer(
            RoleRepo roleRepo,
            UserRepo userRepo,
            CustomerService customerService
    ) {
        this.roleRepo = roleRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        // Insert all roles
        for (Roles roleEnum : Roles.values()) { //  roleEnum means all roles in enum
            String roleName = roleEnum.name();
            if (!roleRepo.existsByName(roleName)) {
                roleRepo.save(new Role(roleName)); // Use constructor with only name
                System.out.println("####### Inserted role: " + roleName);
            }
        }
    }
}
