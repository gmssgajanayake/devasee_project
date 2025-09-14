package com.devasee.product.services;

import com.devasee.product.dto.*;
import com.devasee.product.entity.Printing;
import com.devasee.product.enums.ContainerType;
import com.devasee.product.interfaces.InventoryClient;
import com.devasee.product.repo.PrintRepo;
import com.devasee.product.exception.ProductAlreadyExistsException;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.exception.ServiceUnavailableException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import org.hibernate.exception.DataException;
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

@Service
@Transactional
public class PrintServices {

    private static final Logger log = LoggerFactory.getLogger(PrintServices.class);

    private final PrintRepo printRepo;
    private final ModelMapper modelMapper;
    private final AzureBlobService azureBlobService;
    private final InventoryClient inventoryClient;

    public PrintServices(PrintRepo printRepo, ModelMapper modelMapper,
                         AzureBlobService azureBlobService, InventoryClient inventoryClient) {
        this.printRepo = printRepo;
        this.modelMapper = modelMapper;
        this.azureBlobService = azureBlobService;
        this.inventoryClient = inventoryClient;
    }

    private RetrievePrintDTO sasUrlAndQuantitySetter(Printing printing) {
        RetrievePrintDTO dto = modelMapper.map(printing, RetrievePrintDTO.class);

        String blobName = dto.getImgUrl();
        if (blobName != null && !blobName.isEmpty()) {
            String sasUrl = azureBlobService.generateSasUrl(blobName, ContainerType.PRINTING);
            dto.setImgUrl(sasUrl);
        }

        dto.setStockQuantity(getStockQuantity(dto.getId()));
        return dto;
    }

    private int getStockQuantity(String productId) {
        try {
            return inventoryClient.getStockQuantity(productId);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error fetching quantity from inventory");
        }
    }

    // ------------------ Public ------------------

    public Page<RetrievePrintDTO> getAllPrints(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
            Page<Printing> printPage = printRepo.findAll(pageable);

            Page<RetrievePrintDTO> dtoPage = printPage.map(this::sasUrlAndQuantitySetter);
            if (dtoPage.isEmpty()) {
                throw new ProductNotFoundException("No prints found");
            }
            return dtoPage;
        } catch (DataException e) {
            log.error("### Error fetching all prints", e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    public RetrievePrintDTO getPrintById(String printId) {
        try {
            Printing printing = printRepo.findById(printId)
                    .orElseThrow(() -> new ProductNotFoundException("Print not found with ID: " + printId));
            return sasUrlAndQuantitySetter(printing);
        } catch (DataAccessException e) {
            log.error("### Error fetching print by ID: {}", printId, e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    // ------------------ Admin ------------------

    public CreatePrintDTO savePrint(String printJson, MultipartFile file) {
        CreatePrintDTO createDTO;
        try {
            ObjectMapper mapper = new ObjectMapper();
            createDTO = mapper.readValue(printJson, CreatePrintDTO.class);

            if (printRepo.existsByTitleAndType(createDTO.getTitle(), createDTO.getType())) {
                throw new ProductAlreadyExistsException("Print with same title and type already exists.");
            }
        } catch (Exception e) {
            throw new ServiceUnavailableException("Error mapping JSON to DTO: " + e.getMessage());
        }

        try {
            String fileName = (file != null && !file.isEmpty()) ?
                    azureBlobService.uploadFile(file, ContainerType.PRINTING) : null;
            Printing newPrint = modelMapper.map(createDTO, Printing.class);
            newPrint.setImgUrl(fileName);

            Printing savedPrint = printRepo.save(newPrint);

            // Add inventory
           InventoryRequestDTO inventoryRequestDTO = new InventoryRequestDTO(
                    savedPrint.getId(),
                    createDTO.getStockQuantity()
            );
            inventoryClient.createInventory(inventoryRequestDTO);//

            log.info("### Print saved successfully with ID: {}", savedPrint.getId());
            return createDTO;
        } catch (Exception e) {
            log.error("### Error saving print", e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    // Update Print Details, Cover Image Not Required
    public RetrievePrintDTO updatePrint(String printJson, MultipartFile file) {

        UpdatePrintDTO printDTO;

        try {
            // Convert JSON string to DTO
            ObjectMapper mapper = new ObjectMapper();
            printDTO = mapper.readValue(printJson, UpdatePrintDTO.class);

            // Find existing print or throw custom exception
            Printing existingPrint = printRepo.findById(printDTO.getId())
                    .orElseThrow(() -> new ProductNotFoundException("Print not found"));

            String existingImgUrl = existingPrint.getImgUrl();

            // Map only non-null fields from DTO into existing entity
            modelMapper.map(printDTO, existingPrint);

            log.info("########## file : {}", file);

            if (file != null && !file.isEmpty()) {
                log.info("############### file not null : {}", existingPrint.getImgUrl());
                try {
                    // Delete old image from Azure if exists
                    if (existingImgUrl != null && !existingImgUrl.isEmpty()) {
                        azureBlobService.deleteFile(existingImgUrl, ContainerType.PRINTING);
                    }
                    // Upload new image to Azure Blob Storage
                    String fileName = azureBlobService.uploadFile(file, ContainerType.PRINTING);
                    existingPrint.setImgUrl(fileName);
                } catch (Exception ex) {
                    throw new InternalServerErrorException("Something went wrong when uploading images");
                }
            } else {
                log.info("############### current url : {}", existingPrint.getImgUrl());
                existingPrint.setImgUrl(existingImgUrl);
            }

            Printing savedPrint = printRepo.save(existingPrint);
            log.info("### Print updated successfully with ID: {}", savedPrint.getId());

            return sasUrlAndQuantitySetter(savedPrint);

        } catch (JsonProcessingException e) {
            log.error("### Invalid JSON : {}", e.getMessage());
            throw new BadRequestException("Invalid JSON: " + e.getMessage());
        } catch (DataAccessException e) {
            log.error("### Error updating print with ID: {}", e.getMessage());
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    public Page<RetrievePrintDTO> searchPrintsByTerm(String field, String value, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Printing> printPage;

        switch (field.toLowerCase()) {
            case "title":
                printPage = printRepo.findByTitleContainingIgnoreCase(value, pageable);
                break;
            case "type":
                printPage = printRepo.findByTypeContainingIgnoreCase(value, pageable);
                break;
            default:
                throw new IllegalArgumentException("Invalid search field: " + field);
        }

        if (printPage.isEmpty()) throw new ProductNotFoundException("No prints found for " + field + " : " + value);
        return printPage.map(this::sasUrlAndQuantitySetter);
    }


    // Delete Print Details
    public DeletePrintDTO deletePrint(String printId) {

        // Find existing print or throw exception
        Printing print = printRepo.findById(printId).orElseThrow(
                () -> new ProductNotFoundException("Print not found")
        );

        try {
            // Delete inventory first
            inventoryClient.deleteInventory(printId);

            // Delete image from Azure if exists
            if (print.getImgUrl() != null && !print.getImgUrl().isEmpty()) {
                azureBlobService.deleteFile(print.getImgUrl(), ContainerType.PRINTING);
            }

            // Delete print from database
            printRepo.deleteById(printId);

            log.info("### Print deleted successfully with ID: {}", printId);
        } catch (Exception e) {
            log.error("### Error deleting print with ID: {}", printId, e);
            throw new ServiceUnavailableException("Something went wrong in server");
        }

        // Map deleted entity to DeletePrintDTO for response
        return modelMapper.map(print, DeletePrintDTO.class);
    }

}
