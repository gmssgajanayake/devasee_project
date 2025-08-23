package com.devasee.product.services;

import com.devasee.product.dto.PrintDTO;
import com.devasee.product.entity.Printing;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.exception.ProductAlreadyExistsException;
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

    public List<PrintDTO> getPrintType() {
        try {
            return modelMapper.map(printRepo.findAll(), new TypeToken<List<PrintDTO>>() {}.getType());
        } catch (DataException e) {
            log.error("### Error fetching all prints", e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    public PrintDTO getPrintById(int printId) {
        try {
            Printing printing = printRepo.findById(printId)
                    .orElseThrow(() -> new ProductNotFoundException("Print not found with ID: " + printId));
            return modelMapper.map(printing, PrintDTO.class);
        } catch (DataAccessException e) {
            log.error("### Error fetching print by ID: {}", printId, e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    // --------------------- Admin ---------------------

    public PrintDTO savePrint(PrintDTO printDTO) {
        // you can add a uniqueness check like for ISBN if needed, e.g. by title/type
        try {
            Printing savedPrint = printRepo.save(modelMapper.map(printDTO, Printing.class));
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

            Printing updatedPrint = modelMapper.map(printDTO, Printing.class);
            updatedPrint.setId(existingPrint.getId()); // ensure ID is preserved

            Printing savedPrint = printRepo.save(updatedPrint);
            log.info("### Print updated successfully with ID: {}", savedPrint.getId());
            return modelMapper.map(savedPrint, PrintDTO.class);
        } catch (DataAccessException e) {
            log.error("### Error updating print with ID: {}", printDTO.getId(), e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    // Service
    public boolean deletePrint(int id) {
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
