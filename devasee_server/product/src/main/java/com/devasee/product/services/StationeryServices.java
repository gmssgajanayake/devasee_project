package com.devasee.product.services;

import com.devasee.product.dto.CreateStationeryDTO;
import com.devasee.product.dto.DeleteStationeryDTO;
import com.devasee.product.dto.RetrieveStationeryDTO;
import com.devasee.product.entity.Stationery;
import com.devasee.product.exception.ProductAlreadyExistsException;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.exception.ServiceUnavailableException;
import com.devasee.product.repo.StationeryRepo;
import jakarta.transaction.Transactional;
import org.hibernate.exception.DataException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class StationeryServices {

    private final StationeryRepo stationeryRepo;
    private final ModelMapper modelMapper;

    public StationeryServices(StationeryRepo stationeryRepo, ModelMapper modelMapper) {
        this.stationeryRepo = stationeryRepo;
        this.modelMapper = modelMapper;
    }

    public List<RetrieveStationeryDTO> getAllStationery() {
        try {
            return modelMapper.map(stationeryRepo.findAll(), new TypeToken<List<RetrieveStationeryDTO>>() {
            }.getType());
        } catch (DataException e) {
            throw new ServiceUnavailableException("Server error. Please try again later.");
        }
    }

    public RetrieveStationeryDTO getStationeryById(int id) {
        Stationery stationery = stationeryRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Stationery item not found with ID: " + id));
        return modelMapper.map(stationery, RetrieveStationeryDTO.class);
    }

    public CreateStationeryDTO saveStationery(CreateStationeryDTO dto) {
        if (stationeryRepo.existsByName(dto.getName())) {
            throw new ProductAlreadyExistsException("Stationery with name '" + dto.getName() + "' already exists");
        }
        Stationery entity = modelMapper.map(dto, Stationery.class);
        stationeryRepo.save(entity);
        return dto;
    }

    public RetrieveStationeryDTO updateStationery(RetrieveStationeryDTO dto) {
        Stationery existing = stationeryRepo.findById(dto.getId())
                .orElseThrow(() -> new ProductNotFoundException("Stationery item not found with ID: " + dto.getId()));
        Stationery updated = modelMapper.map(dto, Stationery.class);
        updated.setId(existing.getId());
        Stationery saved = stationeryRepo.save(updated);
        return modelMapper.map(saved, RetrieveStationeryDTO.class);
    }

    public DeleteStationeryDTO deleteStationery(int id) {
        Stationery existing = stationeryRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Stationery item not found with ID: " + id));
        stationeryRepo.delete(existing);
        return modelMapper.map(existing, DeleteStationeryDTO.class);
    }
}