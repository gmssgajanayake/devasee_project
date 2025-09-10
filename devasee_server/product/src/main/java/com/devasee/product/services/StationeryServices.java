package com.devasee.product.services;

import com.devasee.product.dto.*;
import com.devasee.product.entity.Stationery;
import com.devasee.product.exception.ProductAlreadyExistsException;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.exception.ServiceUnavailableException;
import com.devasee.product.interfaces.InventoryClient;
import com.devasee.product.repo.StationeryRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
public class StationeryServices {

    private static final Logger log = LoggerFactory.getLogger(StationeryServices.class);

    private final StationeryRepo stationeryRepo;
    private final ModelMapper modelMapper;
    private final AzureBlobService azureBlobService;
    private final InventoryClient inventoryClient;

    public StationeryServices(
            StationeryRepo stationeryRepo,
            ModelMapper modelMapper,
            AzureBlobService azureBlobService,
            InventoryClient inventoryClient
    ){
        this.stationeryRepo = stationeryRepo;
        this.modelMapper = modelMapper;
        this.azureBlobService = azureBlobService;
        this.inventoryClient = inventoryClient;
    }

    /** Convert Entity → DTO + add SAS URL + stock */
    private RetrieveStationeryDTO addSasUrlAndStock(Stationery stationery){
        RetrieveStationeryDTO dto = modelMapper.map(stationery, RetrieveStationeryDTO.class);

        if (dto.getImgUrl() != null && !dto.getImgUrl().isEmpty()) {
            dto.setImgUrl(azureBlobService.generateSasUrl(dto.getImgUrl()));
        }

        dto.setStockQuantity(getStockQuantity(dto.getId()));
        return dto;
    }

    /** Get stock from Inventory service */
    private int getStockQuantity(String productId){
        try {
            return inventoryClient.getStockQuantity(productId);
        } catch (Exception e){
            log.error("Error fetching stock for productId: {}", productId, e);
            throw new InternalServerErrorException("Error fetching stock from inventory");
        }
    }

    /** Get all stationery (paginated) */
    public Page<RetrieveStationeryDTO> getAllStationery(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Stationery> pageData = stationeryRepo.findAll(pageable);

        if (pageData.isEmpty())
            throw new ProductNotFoundException("No stationery found");

        return pageData.map(this::addSasUrlAndStock);
    }

    /** Get stationery by ID */
    public RetrieveStationeryDTO getStationeryById(String id){
        Stationery stationery = stationeryRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Stationery not found with ID: " + id));
        return addSasUrlAndStock(stationery);
    }

    /** Search stationery by name */
    public Page<RetrieveStationeryDTO> searchStationeryByName(String name, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Stationery> pageData = stationeryRepo.findByNameContainingIgnoreCase(name, pageable);

        if (pageData.isEmpty())
            throw new ProductNotFoundException("No stationery found for name: " + name);

        return pageData.map(this::addSasUrlAndStock);
    }

    /** Save new Stationery + Inventory record */
    public CreateStationeryDTO saveStationery(String stationeryJson, MultipartFile file){
        CreateStationeryDTO dto;
        try {
            ObjectMapper mapper = new ObjectMapper();
            dto = mapper.readValue(stationeryJson, CreateStationeryDTO.class);
        } catch (JsonProcessingException e){
            throw new BadRequestException("Invalid JSON: " + e.getMessage());
        }

        // Check if stationery with same name already exists
        if (stationeryRepo.existsByName(dto.getName())){
            throw new ProductAlreadyExistsException("Stationery with name '" + dto.getName() + "' already exists");
        }

        try {
            String fileName = (file != null) ? azureBlobService.uploadFile(file) : null;

            Stationery stationery = modelMapper.map(dto, Stationery.class);
            stationery.setImgUrl(fileName);

            Stationery saved = stationeryRepo.save(stationery);

            inventoryClient.createInventory(new InventoryRequestDTO(saved.getId(), dto.getInitialQuantity()));

            log.info("Stationery saved with ID: {}", saved.getId());
        } catch (Exception e){
            log.error("Error saving stationery", e);
            throw new ServiceUnavailableException("Something went wrong on the server.");
        }
        return dto;
    }

    /** Update existing stationery */
    public RetrieveStationeryDTO updateStationery(String stationeryJson, MultipartFile file){
        UpdateStationeryDTO dto;
        try {
            dto = new ObjectMapper().readValue(stationeryJson, UpdateStationeryDTO.class);

            Stationery existing = stationeryRepo.findById(dto.getId())
                    .orElseThrow(() -> new ProductNotFoundException("Stationery not found"));

            String existingImgUrl = existing.getImgUrl();
            modelMapper.map(dto, existing);

            if(file != null && !file.isEmpty()){
                if(existingImgUrl != null) azureBlobService.deleteFile(existingImgUrl);
                existing.setImgUrl(azureBlobService.uploadFile(file));
            }

            Stationery saved = stationeryRepo.save(existing);
            return addSasUrlAndStock(saved);

        } catch (JsonProcessingException e){
            throw new BadRequestException("Invalid JSON: " + e.getMessage());
        } catch (DataAccessException e){
            throw new ServiceUnavailableException("Error updating stationery");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Delete stationery by ID */
    public DeleteStationeryDTO deleteStationery(String id){
        Stationery stationery = stationeryRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Stationery not found"));

        try {
            if(stationery.getImgUrl() != null) {
                azureBlobService.deleteFile(stationery.getImgUrl());
            }
            stationeryRepo.deleteById(id);
            return modelMapper.map(stationery, DeleteStationeryDTO.class);
        } catch (Exception e){
            log.error("Error deleting stationery with ID: {}", id, e);
            throw new ServiceUnavailableException("Error deleting stationery");
        }
    }
}
