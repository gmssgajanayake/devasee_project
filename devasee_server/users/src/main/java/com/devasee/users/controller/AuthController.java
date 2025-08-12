package com.devasee.users.controller;

import com.devasee.users.dto.CreateUserDTO;
import com.devasee.users.response.CustomResponse;
import com.devasee.users.service.AdminService;
import com.devasee.users.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/auth/info")
public class AuthController {

    private final CustomerService customerService;
    private final AdminService adminService;

    public AuthController(CustomerService customerService, AdminService adminService) {
        this.customerService = customerService;
        this.adminService = adminService;
    }

    @PostMapping("/save")
    // Saving an user after registering in frontend
    public CustomResponse<Object> saveCustomer(
            HttpServletRequest request
    ) {
        System.out.println("############ controller save");
        // Get the Authorization header
        String authHeader = request.getHeader("Authorization");

        try {
            CreateUserDTO dto = customerService.saveCustomer(authHeader);
            return new CustomResponse<>(true, "Customer created successfully", dto);
        } catch (Exception e){
            System.out.println("########### err save controller: "+e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{userId}/roles")
    public ResponseEntity<List<String>> getUserRole(@PathVariable String userId) {
        try {
            List<String> roles = adminService.getUserRole(userId);
            return ResponseEntity.ok(new ArrayList<>(roles)); // Ensure mutable list
        } catch (Exception e) {
            System.out.println("Error fetching roles, returning default USER role");
            return ResponseEntity.ok(new ArrayList<>(Collections.singletonList("CUSTOMER")));
        }
    }
}
