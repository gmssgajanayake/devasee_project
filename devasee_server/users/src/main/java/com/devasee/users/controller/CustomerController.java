package com.devasee.users.controller;

import com.devasee.users.dto.CreateUserDTO;
import com.devasee.users.response.CustomResponse;
import com.devasee.users.service.AdminService;
import com.devasee.users.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/users/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final AdminService adminService;

    public CustomerController(CustomerService customerService, AdminService adminService) {
        this.customerService = customerService;
        this.adminService = adminService;
    }



    // ------------------------------- User -------------------------------

//    // Saving an user after registering in frontend
//    @PostMapping("/info/save")
//    public CustomResponse<Object> saveCustomer(
//            HttpServletRequest request
//    ) {
//        System.out.println("############ conteoller save");
//        // Get the Authorization header
//        String authHeader = request.getHeader("Authorization");
//
//        CreateUserDTO dto = customerService.saveCustomer(authHeader);
//        return new CustomResponse<>(true, "Customer created successfully", dto);
//    }

    // TODO : update user
    @PutMapping("/info/update")
    public String updateUser(){
        return "updating user";
    }

    // Delete user by header id by user
    @DeleteMapping("/info/delete")
    public ResponseEntity<Void> deleteCustomer(@RequestHeader("X-User-Id") String customerId) {
        System.out.println("######### customerId : "+customerId);
        customerService.deleteUser(customerId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // Delete user by id by user
    // TODO :  testing
    @DeleteMapping("/info/deleteTest/{customerId}")
    public ResponseEntity<Void> deleteCustomerTesting(@PathVariable String customerId) {
        customerService.deleteUser(customerId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
