package com.devasee.product.services;

import com.devasee.product.dto.*;
import com.devasee.product.entity.Stationery;
import com.devasee.product.entity.StationeryBrand;
import com.devasee.product.entity.StationeryCategory;
import com.devasee.product.enums.ContainerType;
import com.devasee.product.exception.ProductAlreadyExistsException;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.exception.ServiceUnavailableException;
import com.devasee.product.interfaces.InventoryClient;
import com.devasee.product.repo.StationeryRepo;
import com.devasee.product.repo.StationeryBrandRepo;
import com.devasee.product.repo.StationeryCategoryRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StationeryServices {

    private static final Logger log = LoggerFactory.getLogger(StationeryServices.class);

    private final StationeryRepo stationeryRepo;
    private final StationeryCategoryRepo categoryRepo;
    private final StationeryBrandRepo brandRepo;
    private final ModelMapper modelMapper;
    private final AzureBlobService azureBlobService;
    private final InventoryClient inventoryClient;

    public StationeryServices(
            StationeryRepo stationeryRepo,
            StationeryCategoryRepo categoryRepo,
            StationeryBrandRepo brandRepo,
            ModelMapper modelMapper,
            AzureBlobService azureBlobService,
            InventoryClient inventoryClient
    ) {
        this.stationeryRepo = stationeryRepo;
        this.categoryRepo = categoryRepo;
        this.brandRepo = brandRepo;
        this.modelMapper = modelMapper;
        this.azureBlobService = azureBlobService;
        this.inventoryClient = inventoryClient;
    }

    // ----------------------- Mapping -----------------------
    private RetrieveStationeryDTO mapToRetrieveDTO(Stationery stationery) {
        RetrieveStationeryDTO dto = modelMapper.map(stationery, RetrieveStationeryDTO.class);

        // main image
        if (stationery.getImgFileName() != null && !stationery.getImgFileName().isEmpty()) {
            dto.setImgUrl(azureBlobService.generateSasUrl(stationery.getImgFileName(), ContainerType.STATIONERY));
        }

        // other images
        List<String> otherUrls = new ArrayList<>();
        if (stationery.getOtherImgFileNames() != null) {
            for (String fileName : stationery.getOtherImgFileNames()) {
                otherUrls.add(azureBlobService.generateSasUrl(fileName, ContainerType.STATIONERY));
            }
        }
        dto.setOtherImgUrls(otherUrls);

        // stock
        dto.setStockQuantity(getStockQuantity(stationery.getId()));

        // category & brand
        dto.setCategory(stationery.getCategory() != null ? stationery.getCategory().getName() : null);
        dto.setBrand(stationery.getBrand() != null ? stationery.getBrand().getName() : null);

        return dto;
    }

    private StationeryCategory getOrCreateCategory(String name) {
        return categoryRepo.findByNameIgnoreCase(name)
                .orElseGet(() -> categoryRepo.save(new StationeryCategory(null, name)));
    }

    private StationeryBrand getOrCreateBrand(String name) {
        return brandRepo.findByNameIgnoreCase(name)
                .orElseGet(() -> brandRepo.save(new StationeryBrand(null, name)));
    }

    private int getStockQuantity(String productId) {
        try {
            return inventoryClient.getStockQuantity(productId);
        } catch (Exception e) {
            log.error("Error fetching stock for productId: {}", productId, e);
            throw new InternalServerErrorException("Error fetching stock from inventory");
        }
    }

    // ----------------------- CRUD -----------------------
    public Page<RetrieveStationeryDTO> getAllStationery(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        Page<Stationery> pageResult = stationeryRepo.findAll(pageable);
        if (pageResult.isEmpty()) throw new ProductNotFoundException("No stationery found");
        return pageResult.map(this::mapToRetrieveDTO);
    }

    public RetrieveStationeryDTO getStationeryById(String id) {
        Stationery stationery = stationeryRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Stationery not found with ID: " + id));
        return mapToRetrieveDTO(stationery);
    }

    public Page<RetrieveStationeryDTO> searchStationeryByTitle(String value, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Stationery> pageResult = stationeryRepo.findByTitleContainingIgnoreCase(value, pageable);
        if (pageResult.isEmpty())
            throw new ProductNotFoundException("No stationery found for title: " + value);
        return pageResult.map(this::mapToRetrieveDTO);
    }

    public CreateStationeryDTO saveStationery(String stationeryJson, MultipartFile mainFile, List<MultipartFile> otherFiles) {
        CreateStationeryDTO dto;

        try {

            dto = new ObjectMapper().readValue(stationeryJson, CreateStationeryDTO.class);
        } catch (JsonProcessingException e) {

            throw new BadRequestException("Invalid JSON: " + e.getMessage());
        }

        if (stationeryRepo.existsByTitle(dto.getTitle()))
            throw new ProductAlreadyExistsException("Stationery with name '" + dto.getTitle() + "' already exists");

        try {

            String mainFileName = (mainFile != null && !mainFile.isEmpty())
                    ? azureBlobService.uploadFile(mainFile, ContainerType.STATIONERY)
                    : null;

            List<String> otherFileNames = new ArrayList<>();
            if (otherFiles != null) {
                for (MultipartFile f : otherFiles) {
                    if (f != null && !f.isEmpty()) {
                        otherFileNames.add(azureBlobService.uploadFile(f, ContainerType.STATIONERY));
                    }
                }
            }

            Stationery stationery = modelMapper.map(dto, Stationery.class);
            stationery.setImgFileName(mainFileName);
            stationery.setOtherImgFileNames(otherFileNames);
            stationery.setCategory(getOrCreateCategory(dto.getCategory().toLowerCase()));
            stationery.setBrand(getOrCreateBrand(dto.getBrand().toLowerCase()));

            Stationery saved = stationeryRepo.save(stationery);
            inventoryClient.createInventory(new InventoryRequestDTO(saved.getId(), dto.getInitialQuantity()));
            log.info("Stationery saved with ID: {}", saved.getId());
            return dto;

        } catch (Exception e) {
            log.error("Error saving stationery", e);
            throw new ServiceUnavailableException("Something went wrong on the server.");
        }
    }

    public Page<RetrieveStationeryDTO> filterStationeryByTerm(String term, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        Double price = null;
        try { price = Double.parseDouble(term); } catch (NumberFormatException ignored) {}

        Page<Stationery> pageResult;
        if (price != null) {
            pageResult = stationeryRepo.findByPrice(price, pageable);
        } else {
            pageResult = stationeryRepo.findByTitleContainingIgnoreCaseOrCategory_NameIgnoreCaseOrBrand_NameIgnoreCase(
                    term, term, term, pageable
            );
        }

        if (pageResult.isEmpty())
            throw new ProductNotFoundException("No stationery found for filter term: " + term);
        return pageResult.map(this::mapToRetrieveDTO);
    }

    public RetrieveStationeryDTO updateStationery(String stationeryJson, MultipartFile mainFile, List<MultipartFile> otherFiles) {
        try {
            UpdateStationeryDTO dto = new ObjectMapper().readValue(stationeryJson, UpdateStationeryDTO.class);
            Stationery existing = stationeryRepo.findById(dto.getId())
                    .orElseThrow(() -> new ProductNotFoundException("Stationery not found"));

            String oldMainFileName = existing.getImgFileName();
            modelMapper.map(dto, existing);

            if (mainFile != null && !mainFile.isEmpty()) {
                if (oldMainFileName != null) azureBlobService.deleteFile(oldMainFileName, ContainerType.STATIONERY);
                existing.setImgFileName(azureBlobService.uploadFile(mainFile, ContainerType.STATIONERY));
            }

            if (otherFiles != null) {
                List<String> currentFiles = existing.getOtherImgFileNames() != null
                        ? existing.getOtherImgFileNames() : new ArrayList<>();
                for (MultipartFile f : otherFiles) {
                    if (f != null && !f.isEmpty())
                        currentFiles.add(azureBlobService.uploadFile(f, ContainerType.STATIONERY));
                }
                existing.setOtherImgFileNames(currentFiles);
            }

            existing.setCategory(getOrCreateCategory(dto.getCategory().toLowerCase()));
            existing.setBrand(getOrCreateBrand(dto.getBrand().toLowerCase()));

            Stationery saved = stationeryRepo.save(existing);
            return mapToRetrieveDTO(saved);

        } catch (JsonProcessingException e) {
            throw new BadRequestException("Invalid JSON: " + e.getMessage());
        } catch (DataAccessException | IOException e) {
            throw new ServiceUnavailableException("Error updating stationery");
        }
    }

    public DeleteStationeryDTO deleteStationery(String id) {
        Stationery stationery = stationeryRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Stationery not found"));

        try {
            inventoryClient.deleteInventory(id);

            if (stationery.getImgFileName() != null)
                azureBlobService.deleteFile(stationery.getImgFileName(), ContainerType.STATIONERY);
            if (stationery.getOtherImgFileNames() != null) {
                for (String fileName : stationery.getOtherImgFileNames())
                    azureBlobService.deleteFile(fileName, ContainerType.STATIONERY);
            }

            stationeryRepo.deleteById(id);
            return modelMapper.map(stationery, DeleteStationeryDTO.class);
        } catch (Exception e) {
            log.error("Error deleting stationery with ID: {}", id, e);
            throw new ServiceUnavailableException("Error deleting stationery");
        }
    }

    public RetrieveStationeryDTO deleteImgFromAzureBlobByFileName(String fileName, String productId) {
        try {
            if (fileName != null && !fileName.isEmpty())
                azureBlobService.deleteFile(fileName, ContainerType.STATIONERY);

            Stationery stationery = stationeryRepo.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Stationery not found with id : " + productId));

            List<String> existingFiles = stationery.getOtherImgFileNames();
            if (existingFiles != null) existingFiles.removeIf(f -> f.equals(fileName));

            stationery.setOtherImgFileNames(existingFiles);
            return mapToRetrieveDTO(stationeryRepo.save(stationery));
        } catch (Exception e) {
            throw new ServiceUnavailableException("Something went wrong in server");
        }
    }
}
