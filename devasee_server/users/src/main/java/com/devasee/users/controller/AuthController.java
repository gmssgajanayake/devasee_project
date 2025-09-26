package com.devasee.users.controller;

import com.devasee.users.dto.CreateUserDTO;
import com.devasee.users.dto.UserDataDTO;
import com.devasee.users.response.CustomResponse;
import com.devasee.users.service.AdminService;
import com.devasee.users.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/users/auth")
public class AuthController {

    private final CustomerService customerService;
    private final AdminService adminService;

    public AuthController(
            CustomerService customerService,
            AdminService adminService
    ) {
        this.customerService = customerService;
        this.adminService = adminService;
    }

    // Saving an user after registering in frontend, default role returning and assigning as CUSTOMER
    // /api/v1/users/auth
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public CustomResponse<Object> createUser(
            HttpServletRequest request
    ) {
        // Get the Authorization header
        String authHeader = request.getHeader("Authorization");

        CreateUserDTO dto = customerService.saveUser(authHeader);
        return new CustomResponse<>(true, "Customer created successfully", dto);
    }

    // TODO : No way in clerk
    // Delete user by user using token, this request header added by apigateway
    // /api/v1/users/auth
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestHeader("X-User-Id") String userId) {
        customerService.deleteUser(userId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // For api service : Return actual role or if not there return default role CUSTOMER
    // /api/v1/users/auth/{userId}/info
    @GetMapping("/{userId}/info")
    public ResponseEntity<UserDataDTO> getUserRole(@PathVariable String userId) {
        UserDataDTO userDataDTO = adminService.getUserInfo(userId);
        return ResponseEntity.ok(userDataDTO);
    }

    // Delete user by id by user
    // /api/v1/users/auth/deleteTest/{customerId}
    // TODO : testing
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/deleteTest/{customerId}")
    public ResponseEntity<Void> deleteCustomerTesting(@PathVariable String customerId) {
        customerService.deleteUser(customerId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
