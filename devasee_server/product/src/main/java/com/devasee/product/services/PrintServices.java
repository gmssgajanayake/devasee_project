package com.devasee.product.services;

import com.devasee.product.dto.PrintDTO;
import com.devasee.product.entity.Printing;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.exception.ServiceUnavailableException;
import com.devasee.product.repo.PrintRepo;
import jakarta.transaction.Transactional;
import org.hibernate.exception.DataException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PrintServices {

    private static final Logger log = LoggerFactory.getLogger(PrintServices.class);

    private final PrintRepo printRepo;
    private final ModelMapper modelMapper;

    public PrintServices(PrintRepo printRepo, ModelMapper modelMapper) {
        this.printRepo = printRepo;
        this.modelMapper = modelMapper;
    }

    // --------------------- Public ---------------------

    public List<PrintDTO> getAllPrints() {
        try {
            return modelMapper.map(printRepo.findAll(), new TypeToken<List<PrintDTO>>() {}.getType());
        } catch (DataException e) {
            log.error("### Error fetching all prints", e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    public PrintDTO getPrintById(String printId) {
        try {
            Printing printing = printRepo.findById(printId)
                    .orElseThrow(() -> new ProductNotFoundException("Print not found with ID: " + printId));
            return modelMapper.map(printing, PrintDTO.class);
        } catch (DataAccessException e) {
            log.error("### Error fetching print by ID: {}", printId, e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    public List<PrintDTO> getPrintsByType(String type) {
        try {
            return modelMapper.map(printRepo.findByType(type), new TypeToken<List<PrintDTO>>() {}.getType());
        } catch (Exception e) {
            log.error("### Error fetching prints by type: {}", type, e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    public List<PrintDTO> searchPrintsByTitle(String keyword) {
        try {
            return modelMapper.map(printRepo.findByTitleContainingIgnoreCase(keyword), new TypeToken<List<PrintDTO>>() {}.getType());
        } catch (Exception e) {
            log.error("### Error searching prints by title: {}", keyword, e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    public List<PrintDTO> getPrintsByMaterial(String material) {
        try {
            return modelMapper.map(printRepo.findByMaterial(material), new TypeToken<List<PrintDTO>>() {}.getType());
        } catch (Exception e) {
            log.error("### Error fetching prints by material: {}", material, e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    public List<PrintDTO> getPrintsCheaperThan(double price) {
        try {
            return modelMapper.map(printRepo.findByPriceLessThan(price), new TypeToken<List<PrintDTO>>() {}.getType());
        } catch (Exception e) {
            log.error("### Error fetching prints cheaper than {}", price, e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    public List<PrintDTO> getAvailableStock(int minStock) {
        try {
            return modelMapper.map(printRepo.findByStockQuantityGreaterThan(minStock), new TypeToken<List<PrintDTO>>() {}.getType());
        } catch (Exception e) {
            log.error("### Error fetching prints with stock greater than {}", minStock, e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    // --------------------- Admin ---------------------

    public PrintDTO savePrint(PrintDTO printDTO) {
        try {
            if (printRepo.existsByTitleAndType(printDTO.getTitle(), printDTO.getType())) {
                throw new ServiceUnavailableException("Print with same title and type already exists.");
            }

            Printing entityToSave = modelMapper.map(printDTO, Printing.class);
            Printing savedPrint = printRepo.save(entityToSave);
            log.info("### Print saved successfully with ID: {}", savedPrint.getId());

            return modelMapper.map(savedPrint, PrintDTO.class);
        } catch (Exception e) {
            log.error("### Error saving print: {}", printDTO, e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    public PrintDTO updatePrint(PrintDTO printDTO) {
        try {
            Printing existingPrint = printRepo.findById(printDTO.getId())
                    .orElseThrow(() -> new ProductNotFoundException("Print not found with ID: " + printDTO.getId()));

            Printing updatedEntity = modelMapper.map(printDTO, Printing.class);
            updatedEntity.setId(existingPrint.getId());

            Printing savedPrint = printRepo.save(updatedEntity);
            log.info("### Print updated successfully with ID: {}", savedPrint.getId());

            return modelMapper.map(savedPrint, PrintDTO.class);
        } catch (DataAccessException e) {
            log.error("### Error updating print with ID: {}", printDTO.getId(), e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    public boolean deletePrint(String id) {
        if (!printRepo.existsById(id)) {
            throw new ProductNotFoundException("Print not found with ID: " + id);
        }
        try {
            printRepo.deleteById(id);
            log.info("### Print deleted successfully with ID: {}", id);
            return true;
        } catch (Exception e) {
            log.error("### Error deleting print with ID: {}", id, e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }
}
