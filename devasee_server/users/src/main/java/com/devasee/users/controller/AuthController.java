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
@RequestMapping("api/v1/users/auth")
public class AuthController {

    private final CustomerService customerService;
    private final AdminService adminService;

    public AuthController(CustomerService customerService, AdminService adminService) {
        this.customerService = customerService;
        this.adminService = adminService;
    }




    // Saving an user after registering in frontend, default role returning and assigning as CUSTOMER
    @PostMapping("/save")
    public CustomResponse<Object> saveCustomer(
            HttpServletRequest request
    ) {
        // Get the Authorization header
        String authHeader = request.getHeader("Authorization");

        CreateUserDTO dto = customerService.saveCustomer(authHeader);
        return new CustomResponse<>(true, "Customer created successfully", dto);
    }

    // TODO : update user by user
    @PutMapping("/update")
    public String updateUser(){
        return "updating user";
    }

    // Delete user by user
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCustomer(@RequestHeader("X-User-Id") String customerId) {
        customerService.deleteUser(customerId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // Return actual role or if not there return default role CUSTOMER
    @GetMapping("/{userId}/roles")
    public ResponseEntity<List<String>> getUserRole(@PathVariable String userId) {
        List<String> roles = adminService.getUserRole(userId);
        return ResponseEntity.ok(new ArrayList<>(roles)); // Ensure mutable list
    }

    // Delete user by id by user
    // TODO :  testing
    @DeleteMapping("/deleteTest/{customerId}")
    public ResponseEntity<Void> deleteCustomerTesting(@PathVariable String customerId) {
        customerService.deleteUser(customerId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
