package com.devasee.users.service;

import com.devasee.users.dto.AdminDTO;
import com.devasee.users.entity.Admin;
import com.devasee.users.repository.AdminRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<AdminDTO> getAllAdmins() {
        try {
            return modelMapper.map(adminRepository.findAll(), new TypeToken<List<AdminDTO>>() {}.getType());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch admins", e);
        }
    }

    public AdminDTO getAdminById(Long adminId) {
        try {
            Optional<Admin> admin = adminRepository.findById(adminId);
            return admin.map(value -> modelMapper.map(value, AdminDTO.class)).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch admin with ID: " + adminId, e);
        }
    }

    public AdminDTO saveAdmin(AdminDTO adminDTO) {
        try {
            adminRepository.save(modelMapper.map(adminDTO, Admin.class));
            return adminDTO;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save admin", e);
        }
    }

    public AdminDTO updateAdmin(AdminDTO adminDTO) {
        try {
            adminRepository.save(modelMapper.map(adminDTO, Admin.class));
            return adminDTO;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update admin", e);
        }
    }


    public boolean deleteAdminById(Long adminId) {
        Optional<Admin> admin = adminRepository.findById(adminId);
        if (admin.isPresent()) {
            adminRepository.delete(admin.get());
            return true;
        }
        return false;
    }
}
