package com.devasee.product.services.printing;

import com.devasee.product.dto.printing.CreatePrintDTO;
import com.devasee.product.dto.printing.DeletePrintDTO;
import com.devasee.product.dto.printing.RetrievePrintDTO;
import com.devasee.product.dto.printing.UpdatePrintDTO;
import com.devasee.product.dto.stationery.InventoryRequestDTO;
import com.devasee.product.entity.printing.PrintProductType;
import com.devasee.product.entity.printing.Printing;
import com.devasee.product.entity.printing.PrintingCategory;
import com.devasee.product.entity.printing.PrintingMaterial;
import com.devasee.product.enums.ContainerType;
import com.devasee.product.interfaces.InventoryClient;
import com.devasee.product.repo.printing.PrintRepo;
import com.devasee.product.repo.printing.PrintProductTypeRepo;
import com.devasee.product.exception.ProductAlreadyExistsException;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.exception.ServiceUnavailableException;
import com.devasee.product.repo.printing.PrintingCategoryRepo;
import com.devasee.product.repo.printing.PrintingMaterialRepo;
import com.devasee.product.services.AzureBlobService;
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
    private final PrintingMaterialRepo printingMaterialRepo;




    public PrintServices(PrintRepo printRepo,
                         ModelMapper modelMapper,
                         AzureBlobService azureBlobService,
                         InventoryClient inventoryClient,
                         ObjectMapper objectMapper,
                         PrintProductTypeRepo printProductTypeRepo,
                         PrintingCategoryRepo printingCategoryRepo,
                         PrintingMaterialRepo printingMaterialRepo) {
        this.printRepo = printRepo;
        this.modelMapper = modelMapper;
        this.azureBlobService = azureBlobService;
        this.inventoryClient = inventoryClient;
        this.objectMapper = objectMapper;
        this.printProductTypeRepo = printProductTypeRepo;
        this.printingCategoryRepo = printingCategoryRepo;
        this.printingMaterialRepo = printingMaterialRepo;
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

            // Validate PrintProductType
            PrintProductType typeEntity = printProductTypeRepo.findByName(createDTO.getTypes())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid print type: " + createDTO.getTypes()));

            // Validate PrintingCategory
            PrintingCategory categoryEntity = printingCategoryRepo.findByName(createDTO.getCategory())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid print category: " + createDTO.getCategory()));

            // Validate PrintingMaterial
            PrintingMaterial materialEntity = printingMaterialRepo.findByName(createDTO.getMaterial())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid print material: " + createDTO.getMaterial()));

            // Check for duplicate (title + type)
            if (printRepo.existsByTitleAndTypes(createDTO.getTitle(), typeEntity)) {
                throw new ProductAlreadyExistsException("Print with same title and type already exists.");
            }

            // ✅ Attach resolved names into DTO for consistency
            createDTO.setTypes(typeEntity.getName());
            createDTO.setCategory(categoryEntity.getName());
            createDTO.setMaterial(materialEntity.getName());

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

            // Map DTO → Entity
            Printing newPrint = modelMapper.map(createDTO, Printing.class);

            // Store only the filename, not SAS URL
            newPrint.setImgFileName(fileName);

            // Resolve and attach associations again to entity
            PrintProductType typeEntity = printProductTypeRepo.findByName(createDTO.getTypes())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid print type: " + createDTO.getTypes()));
            newPrint.setTypes(typeEntity);

            PrintingCategory categoryEntity = printingCategoryRepo.findByName(createDTO.getCategory())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid print category: " + createDTO.getCategory()));
            newPrint.setCategory(categoryEntity);

            PrintingMaterial materialEntity = printingMaterialRepo.findByName(createDTO.getMaterial())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid print material: " + createDTO.getMaterial()));
            newPrint.setMaterial(materialEntity);

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
            printDTO = objectMapper.readValue(printJson, UpdatePrintDTO.class);

            // Find existing print or throw custom exception
            Printing existingPrint = printRepo.findById(printDTO.getId())
                    .orElseThrow(() -> new ProductNotFoundException("Print not found"));

            String existingFileName = existingPrint.getImgFileName();

            // Map simple fields (ignore associations)
            modelMapper.map(printDTO, existingPrint);

            // Handle associations: type
            if (printDTO.getTypes() != null) {
                PrintProductType typeEntity = printProductTypeRepo.findByName(printDTO.getTypes())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid print type: " + printDTO.getTypes()));
                existingPrint.setTypes(typeEntity);
            }

            // Handle associations: category
            if (printDTO.getCategory() != null) {
                PrintingCategory categoryEntity = printingCategoryRepo.findByName(printDTO.getCategory())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid print category: " + printDTO.getCategory()));
                existingPrint.setCategory(categoryEntity);
            }

            // Handle image
            if (file != null && !file.isEmpty()) {
                try {
                    if (existingFileName != null && !existingFileName.isEmpty()) {
                        azureBlobService.deleteFile(existingFileName, ContainerType.PRINTING);
                    }
                    String fileName = azureBlobService.uploadFile(file, ContainerType.PRINTING);
                    existingPrint.setImgFileName(fileName); // ✅ store filename only
                } catch (Exception ex) {
                    throw new InternalServerErrorException("Error when uploading new image");
                }
            } else {
                existingPrint.setImgFileName(existingFileName); // keep old filename
            }

            // Save updated entity
            Printing savedPrint = printRepo.save(existingPrint);
            log.info("### Print updated successfully with ID: {}", savedPrint.getId());

            // Return enriched DTO
            return sasUrlAndQuantitySetter(savedPrint);

        } catch (JsonProcessingException e) {
            log.error("### Invalid JSON : {}", e.getMessage());
            throw new BadRequestException("Invalid JSON: " + e.getMessage());
        } catch (DataAccessException e) {
            log.error("### Error updating print: {}", e.getMessage());
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }


    public Page<RetrievePrintDTO> searchPrintsByTerm(String field, String value, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Printing> printPage = switch (field.toLowerCase()) {
                case "title" -> printRepo.findByTitleContainingIgnoreCase(value, pageable);
                case "type" -> printRepo.findByTypes_NameContainingIgnoreCase(value, pageable);
                case "material" -> printRepo.findByMaterial_NameContainingIgnoreCase(value, pageable);
                case "size" -> printRepo.findBySizeContainingIgnoreCase(value, pageable);
                case "color" -> printRepo.findByColorsContainingIgnoreCase(value, pageable);
                default -> throw new IllegalArgumentException("Unsupported search field: " + field +
                        ". Allowed fields: title, type, material, size, color");
            };

            if (printPage.isEmpty()) {
                throw new ProductNotFoundException("No prints found for " + field + " : " + value);
            }

            return printPage.map(this::sasUrlAndQuantitySetter);

        } catch (IllegalArgumentException | ProductNotFoundException e) {
            log.warn("### Search issue: {}", e.getMessage());
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
