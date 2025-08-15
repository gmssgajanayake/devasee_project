package com.devasee.users.init;

import com.devasee.users.entity.Role;
import com.devasee.users.enums.Roles;
import com.devasee.users.repository.RoleRepo;
import com.devasee.users.repository.UserRepo;
import com.devasee.users.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

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
                log.info("####### DataInitializer Inserted role: {}", roleName);
            }
        }
    }
}
