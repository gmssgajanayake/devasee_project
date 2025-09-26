package com.devasee.users.controller;

import com.devasee.users.dto.AdminDTO;
import com.devasee.users.dto.PromoteDemoteAsAdminDTO;
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

    // Get admin name by id
    // /api/v1/users/admins/{userId}/name
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}/name")
    public String getAdminNameById(
            @PathVariable String userId
    ) {
        return adminService.getAdminNameById(userId);
    }

    // Promote/Create existing user as admin or create a new admin
    // /api/v1/users/admins
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CustomResponse<AdminDTO> promoteAsAdmin(
            @RequestBody PromoteDemoteAsAdminDTO promoteDemoteAdminDTO
            ) {
        AdminDTO adminDTO = adminService.promoteAsAdmin(promoteDemoteAdminDTO);
        return new CustomResponse<>(
                true,
                String.format("User %s promoted successfully", promoteDemoteAdminDTO.getEmail()),
                adminDTO
        );
    }

    // Demote an admin as user by email, remove admin role
    // /api/v1/users/admins
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public CustomResponse<Object> demoteAdmin(@RequestBody PromoteDemoteAsAdminDTO promoteDemoteAsAdminDTO) {

        AdminDTO dto = adminService.demoteAdmin(promoteDemoteAsAdminDTO.getEmail());
        return new CustomResponse<>(
                true,
                "Admin demoted successfully.",
                dto
        );
    }

    // After admin login to access admin page this end point call, get permission to access it
    // POST /api/v1/users/admins/login
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/login")
    public CustomResponse<Boolean> adminLogin(){
        return new CustomResponse<>(
                true,
                "Admin",
                true
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
    // POST /api/v1/users/admins/suspend?userId=<userId>
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/suspend")
    public CustomResponse<RetrieveUserDTO> suspendUserById(@RequestParam String userId){
        RetrieveUserDTO retrieveUserDTO = customerService.suspendUser(userId);
        return new CustomResponse<>(true, "User suspended successful", retrieveUserDTO);
    }

    // Suspend a user by account status
    // POST /api/v1/users/admins/active?userId=<userId>
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/active")
    public CustomResponse<RetrieveUserDTO> activeUserById(@RequestParam String userId){
        RetrieveUserDTO retrieveUserDTO = customerService.activeUser(userId);
        return new CustomResponse<>(true, "User Activated successful", retrieveUserDTO);
    }

    // TODO : should be private
    @GetMapping("/internal/{userId}/status")
    public String getUserAccountStatus(@PathVariable String userId){
       return adminService.getUserAccountStatus(userId);
    }
}

