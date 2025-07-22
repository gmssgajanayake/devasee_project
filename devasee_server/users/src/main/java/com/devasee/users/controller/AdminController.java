package com.devasee.users.controller;

import com.devasee.users.dto.AdminDTO;
import com.devasee.users.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/user/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/allAdmin")
    public List<AdminDTO> getAllAdmin() {
        try {
            return adminService.getAllAdmins();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all admins", e);
        }
    }

    @GetMapping("/{adminId}")
    public AdminDTO getAdminById(@PathVariable Long adminId) {
        try {
            return adminService.getAdminById(adminId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching admin by ID: " + adminId, e);
        }
    }

    @PostMapping("/addAdmin")
    public AdminDTO saveAdmin(@RequestBody AdminDTO adminDTO) {
        try {
            return adminService.saveAdmin(adminDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error saving admin", e);
        }
    }

    @PutMapping("/updateAdmin")
    public AdminDTO updateAdmin(@RequestBody AdminDTO adminDTO) {
        try {
            return adminService.updateAdmin(adminDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error updating admin", e);
        }
    }


    @DeleteMapping("/deleteAdmin/{adminId}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long adminId) {
        try {
            boolean deleted = adminService.deleteAdminById(adminId);
            if (deleted) {
                return ResponseEntity.ok("Admin deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting admin: " + e.getMessage());
        }
    }
}
