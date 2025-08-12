package com.devasee.users.controller;

import com.devasee.users.dto.AdminDTO;
import com.devasee.users.dto.PromoteAsAdminDTO;
import com.devasee.users.dto.RetrieveUserDTO;
import com.devasee.users.response.CustomResponse;
import com.devasee.users.service.AdminService;
import com.devasee.users.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/users/admin")
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

    // Get all admins
    @GetMapping("/allAdmins")
    public CustomResponse<List<AdminDTO>> getAllAdmin() {
        List<AdminDTO> admins = adminService.getAllAdmins();
        return new CustomResponse<>(
                false,
                "All admin returned successfully, No of admins : "+admins.toArray().length,
                admins
        );
    }

    // Promote existing user as admin or create a new admin
    @PostMapping("/promote")
    public CustomResponse<AdminDTO> promoteAsAdmin(@RequestBody PromoteAsAdminDTO promoteDemoteAdminDTO) {
        AdminDTO adminDTO = adminService.promoteAsAdmin(promoteDemoteAdminDTO);
        return new CustomResponse<>(
                false,
                String.format("User <%s> promoted successfully", promoteDemoteAdminDTO.getEmail()),
                adminDTO
        );
    }

    // Demote an admin as user by email
    @PostMapping("/demote/{email}")
    public CustomResponse<Object> demoteAdmin(@RequestBody PromoteAsAdminDTO promoteDemoteAdminDTO) {

        AdminDTO dto = adminService.demoteAdmin(promoteDemoteAdminDTO.getEmail());
        return new CustomResponse<>(
                false,
                "Admin deleted successfully.",
                dto
        );
    }



    // ------------------------------- User management by admin -------------------------------

    // Get all users both customers, admins
    @GetMapping("/allUsers")
    public CustomResponse<List<RetrieveUserDTO>> getAllUsers() {
        List<RetrieveUserDTO> customers = customerService.getAllUsers();
        return new CustomResponse<>(
                true,
                "Customers retrieved successfully, No of users : "+customers.toArray().length,
                customers
        );
    }

    // Get user by user id
    @GetMapping("/{userId}")
    public CustomResponse<RetrieveUserDTO> getCustomerById(@PathVariable String userId) {
        RetrieveUserDTO customer = customerService.getUserById(userId);
        return new CustomResponse<>(true, "Customer retrieved successfully", customer);
    }

    // Search user by a term email, first name, last name
    @GetMapping("/search/{term}")
    public CustomResponse<Object> findUser(@PathVariable String term) {
        return new CustomResponse<>(true, term, null);
    }

    // Suspend an user by account status
    @PostMapping("/suspend/{userId}")
    public CustomResponse<RetrieveUserDTO> suspendUserByEmail(@PathVariable String userId){
        RetrieveUserDTO retrieveUserDTO = customerService.suspendUser(userId);
        return new CustomResponse<>(true, "User suspended successful", retrieveUserDTO);
    }
}

