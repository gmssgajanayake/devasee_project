package com.devasee.product.services;

import com.devasee.product.dto.*;
import com.devasee.product.entity.PrintProductType;
import com.devasee.product.entity.Printing;
import com.devasee.product.entity.PrintingCategory;
import com.devasee.product.enums.ContainerType;
import com.devasee.product.interfaces.InventoryClient;
import com.devasee.product.repo.PrintRepo;
import com.devasee.product.repo.PrintProductTypeRepo;
import com.devasee.product.exception.ProductAlreadyExistsException;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.exception.ServiceUnavailableException;
import com.devasee.product.repo.PrintingCategoryRepo;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class PrintServices {

    private static final Logger log = LoggerFactory.getLogger(PrintServices.class);

    private final PrintRepo printRepo;
    private final ModelMapper modelMapper;
    private final AzureBlobService azureBlobService;
    private final InventoryClient inventoryClient;
    private final ObjectMapper objectMapper;
    private final PrintProductTypeRepo printProductTypeRepo;
    private final PrintingCategoryRepo printingCategoryRepo;


    public PrintServices(PrintRepo printRepo,
                         ModelMapper modelMapper,
                         AzureBlobService azureBlobService,
                         InventoryClient inventoryClient,
                         ObjectMapper objectMapper,
                         PrintProductTypeRepo printProductTypeRepo,
                         PrintingCategoryRepo printingCategoryRepo) {
        this.printRepo = printRepo;
        this.modelMapper = modelMapper;
        this.azureBlobService = azureBlobService;
        this.inventoryClient = inventoryClient;
        this.objectMapper = objectMapper;
        this.printProductTypeRepo = printProductTypeRepo;
        this.printingCategoryRepo = printingCategoryRepo;
    }


    private RetrievePrintDTO sasUrlAndQuantitySetter(Printing printing) {
        RetrievePrintDTO dto = modelMapper.map(printing, RetrievePrintDTO.class);

        // --- Cover image ---
        try {
            String blobName = printing.getImgFileName(); // stored as filename
            if (blobName != null && !blobName.isEmpty()) {
                log.info("### main file name : {}", blobName);
                String sasUrl = azureBlobService.generateSasUrl(blobName, ContainerType.PRINTING);
                dto.setImgUrl(sasUrl);
            }
        } catch (Exception e) {
            log.error("### Error generating SAS URL for print cover ID {}: {}", dto.getId(), e.getMessage());
            dto.setImgUrl(null);
        }

        // --- Other images ---
        try {
            List<String> otherImgUrls = new ArrayList<>();
            List<String> storedFileNames = printing.getOtherImgFileNames();
            if (storedFileNames != null && !storedFileNames.isEmpty()) {
                for (String fileName : storedFileNames) {
                    if (fileName != null && !fileName.isEmpty()) {
                        log.info("### other file names : {}", fileName);
                        try {
                            String otherSasUrl = azureBlobService.generateSasUrl(fileName, ContainerType.PRINTING);
                            otherImgUrls.add(otherSasUrl);
                        } catch (Exception innerEx) {
                            log.error("### Error generating SAS URL for other image {} in print ID {}: {}", fileName, dto.getId(), innerEx.getMessage());
                        }
                    }
                }
            }
            dto.setOtherImgUrls(otherImgUrls);
        } catch (Exception e) {
            log.error("### Error processing other images for print ID {}: {}", dto.getId(), e.getMessage());
            dto.setOtherImgUrls(Collections.emptyList());
        }

        // --- Stock quantity ---
        try {
            dto.setStockQuantity(getStockQuantity(dto.getId()));
        } catch (Exception e) {
            log.error("### Error fetching stock quantity for print ID {}: {}", dto.getId(), e.getMessage());
            dto.setStockQuantity(0);
        }

        // --- Metadata ---
        dto.setCategory(printing.getCategory() != null ? printing.getCategory().getName() : null);
        dto.setTypes(printing.getTypes() != null ? printing.getTypes().getName() : null);

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
// Save new print type
    public CreatePrintDTO savePrint(String printJson, MultipartFile file) {
        CreatePrintDTO createDTO;

        // Step 1: Parse JSON into DTO and check duplicates
        try {
            createDTO = objectMapper.readValue(printJson, CreatePrintDTO.class);

            // Find the PrintProductType entity
            PrintProductType typeEntity = printProductTypeRepo.findByName(createDTO.getTypes())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid print type: " + createDTO.getTypes()));

            // Find the PrintingCategory entity
            PrintingCategory categoryEntity = printingCategoryRepo.findByName(createDTO.getCategory())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid print category: " + createDTO.getCategory()));

            // Check for duplicate
            if (printRepo.existsByTitleAndTypes(createDTO.getTitle(), typeEntity)) {
                throw new ProductAlreadyExistsException("Print with same title and type already exists.");
            }

            // ✅ attach resolved entities into DTO for later mapping
            createDTO.setTypes(typeEntity.getName());
            createDTO.setCategory(categoryEntity.getName());

        } catch (JsonProcessingException e) {
            log.error("### Invalid JSON for print: {}", e.getMessage());
            throw new BadRequestException("Invalid JSON: " + e.getMessage());
        } catch (Exception e) {
            log.error("### Error processing print JSON: {}", e.getMessage(), e);
            throw new ServiceUnavailableException("Error processing print data");
        }

        // Step 2: Upload image, map DTO to entity, save, and add to inventory
        try {
            String fileName = (file != null && !file.isEmpty())
                    ? azureBlobService.uploadFile(file, ContainerType.PRINTING)
                    : null;

            // Map DTO to entity
            Printing newPrint = modelMapper.map(createDTO, Printing.class);

            // Store only the filename, not SAS URL
            newPrint.setImgFileName(fileName);

            // Set associations manually
            PrintProductType typeEntity = printProductTypeRepo.findByName(createDTO.getTypes())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid print type: " + createDTO.getTypes()));
            newPrint.setTypes(typeEntity);

            PrintingCategory categoryEntity = printingCategoryRepo.findByName(createDTO.getCategory())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid print category: " + createDTO.getCategory()));
            newPrint.setCategory(categoryEntity);

            // Save entity
            Printing savedPrint = printRepo.save(newPrint);

            // Add to inventory
            InventoryRequestDTO inventoryRequestDTO = new InventoryRequestDTO(
                    savedPrint.getId(),
                    createDTO.getInitialQuantity()
            );
            inventoryClient.createInventory(inventoryRequestDTO);

            log.info("### Print saved successfully with ID: {}", savedPrint.getId());

        } catch (Exception e) {
            log.error("### Error saving print", e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }

        // Return the input DTO (like saveBook does)
        return createDTO;
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
                    // Delete existing image from Azure
                    if (existingImgUrl != null && !existingImgUrl.isEmpty()) {
                        azureBlobService.deleteFile(existingImgUrl, ContainerType.PRINTING);
                    }

                    // Upload new image to Azure Blob Storage
                    String fileName = azureBlobService.uploadFile(file, ContainerType.PRINTING);
                    existingPrint.setImgUrl(fileName); // Set uploaded image file name
                } catch (Exception exception) {
                    throw new InternalServerErrorException("Something went wrong when uploading images");
                }
            } else {
                log.info("############### current url : {} ", existingPrint.getImgUrl());
                existingPrint.setImgUrl(existingImgUrl);
            }

            // Save updated print
            Printing savedPrint = printRepo.save(existingPrint);
            log.info("### Print updated successfully with ID: {}", savedPrint.getId());

            // Add SAS URL and update quantity if needed
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
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Printing> printPage = switch (field.toLowerCase()) {
                case "title" -> printRepo.findByTitleContainingIgnoreCase(value, pageable);
                case "type" -> printRepo.findByTypesContainingIgnoreCase(value, pageable);
                case "material" -> printRepo.findByMaterialContainingIgnoreCase(value, pageable);
                case "size" -> printRepo.findBySizeContainingIgnoreCase(value, pageable);
                case "color" -> printRepo.findByColorsContainingIgnoreCase(value, pageable);
                default -> throw new IllegalArgumentException("Unsupported search field: " + field +
                        ". Allowed fields: title, type, material, size, color");
            };

            if (printPage.isEmpty()) {
                throw new ProductNotFoundException("No prints found for " + field + " : " + value);
            }

            // Map to DTO with SAS URL and stock quantity
            return printPage.map(this::sasUrlAndQuantitySetter);

        } catch (IllegalArgumentException e) {
            log.error("### Invalid search field: {}", e.getMessage());
            throw e;

        } catch (ProductNotFoundException e) {
            log.warn("### No prints found: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("### Error searching prints: {}", e.getMessage(), e);
            throw new ServiceUnavailableException("Something went wrong while searching prints. Please try again later.");
        }
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
