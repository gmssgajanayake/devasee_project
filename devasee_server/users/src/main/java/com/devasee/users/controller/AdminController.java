package com.devasee.users.controller;

import com.devasee.users.dto.AdminDTO;
import com.devasee.users.dto.PromoteAsAdminDTO;
import com.devasee.users.dto.RetrieveUserDTO;
import com.devasee.users.response.CustomResponse;
import com.devasee.users.service.AdminService;
import com.devasee.users.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/users/admins")
public class AdminController {

    private final AdminService adminService;
    private final CustomerService customerService;

    public AdminController(
            AdminService adminService,
            CustomerService customerService
    ) {
        this.adminService = adminService;
        this.customerService = customerService;
    }

    // --------------------- Manage Admins Info ------------------------------

    // Get all admins by page
    // /api/v1/admins?page=0&size=20
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public CustomResponse<Page<AdminDTO>> getAllAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<AdminDTO> adminsPage = adminService.getAllAdmins(page,size);
        return new CustomResponse<>(
                true,
                "Admins returned successfully",
                adminsPage
        );
    }

    // Get admin by id
    // /api/v1/users/admins/{userId}
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public CustomResponse<AdminDTO> getAdminById(
            @PathVariable String userId
    ) {
        AdminDTO admin = adminService.getAdminById(userId);
        return new CustomResponse<>(
                true,
                "Admin returned successfully",
                admin
        );
    }

    // Promote/Create existing user as admin or create a new admin
    // /api/v1/users/admins
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CustomResponse<AdminDTO> promoteAsAdmin(
            @RequestBody PromoteAsAdminDTO promoteDemoteAdminDTO
            ) {
        AdminDTO adminDTO = adminService.promoteAsAdmin(promoteDemoteAdminDTO);
        return new CustomResponse<>(
                true,
                String.format("User <%s> promoted successfully", promoteDemoteAdminDTO.getEmail()),
                adminDTO
        );
    }

    // Demote an admin as user by email, remove admin role
    // /api/v1/users/admins?email=abc@gmail.com
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public CustomResponse<Object> demoteAdmin(@RequestParam String email) {

        AdminDTO dto = adminService.demoteAdmin(email);
        return new CustomResponse<>(
                true,
                "Admin demoted successfully.",
                dto
        );
    }

    // ------------------------------- User Management By Admin -------------------------------

    // Get all users both customers, admins
    // /api/v1/users/admins/all
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public CustomResponse<Page<RetrieveUserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<RetrieveUserDTO> users = customerService.getAllUsers(page, size);
        return new CustomResponse<>(
                true,
                "All users retrieved successfully",
                users
        );
    }

    // Get user by user id
    // /api/v1/users/admins/any/{userId}
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public CustomResponse<RetrieveUserDTO> getCustomerById(@PathVariable String userId) {
        RetrieveUserDTO customer = customerService.getUserById(userId);
        return new CustomResponse<>(true, "User retrieved successfully", customer);
    }

    // Search user by a term email
    // /api/v1/users/admins/search?email=abc@gmail.com&page=0&size=20
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public CustomResponse<Page<AdminDTO>> searchUser(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<AdminDTO> users = adminService.searchUserByEmail(email, page, size);
        return new CustomResponse<>(true, email, users);
    }

    // Suspend a user by account status
    // /api/v1/users/admins/suspend
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/suspend")
    public CustomResponse<RetrieveUserDTO> suspendUserByEmail(@RequestParam String userId){
        RetrieveUserDTO retrieveUserDTO = customerService.suspendUser(userId);
        return new CustomResponse<>(true, "User suspended successful", retrieveUserDTO);
    }

    // TODO : should be private
    @GetMapping("/internal/{userId}/status")
    public String getUserAccountStatus(@PathVariable String userId){
       return adminService.getUserAccountStatus(userId);
    }
}

