package com.devasee.product.controller;

import com.devasee.product.dto.CreatePrintDTO;
import com.devasee.product.dto.DeletePrintDTO;
import com.devasee.product.dto.RetrievePrintDTO;
import com.devasee.product.response.CustomResponse;
import com.devasee.product.services.PrintServices;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/product/printing")
public class PrintController {

    private final PrintServices printServices;

    public PrintController(PrintServices printServices) {
        this.printServices = printServices;
    }

    // --------------------- Public ---------------------

    // GET /api/v1/product/printing?page=0&size=20
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public CustomResponse<Page<RetrievePrintDTO>> getAllPrints(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<RetrievePrintDTO> printPage = printServices.getAllPrints(page, size);
        return new CustomResponse<>(true, "Prints found", printPage);
    }

    // GET /api/v1/product/printing/{printId}
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{printId}")
    public CustomResponse<RetrievePrintDTO> getPrintById(@PathVariable String printId) {
        RetrievePrintDTO printDTO = printServices.getPrintById(printId);
        return new CustomResponse<>(true, "Print found", printDTO);
    }

    // GET /api/v1/product/printing/search?field=type&value=mug&page=0&size=10
    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public CustomResponse<Page<RetrievePrintDTO>> searchPrintsByTerm(
            @RequestParam String field,
            @RequestParam String value,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<RetrievePrintDTO> printPage = printServices.searchPrintsByTerm(field, value, page, size);
        return new CustomResponse<>(
                true,
                "Prints by " + field + " : " + value,
                printPage
        );
    }

    // --------------------- Admin ---------------------

    // Save new print type
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CustomResponse<CreatePrintDTO> savePrint(
            @RequestParam("print") String printJson,
            @RequestParam("file") MultipartFile file
    ) {
        CreatePrintDTO dtoResponse = printServices.savePrint(printJson, file);
        return new CustomResponse<>(true, "Print saved successfully", dtoResponse);
    }

    // Update existing print type
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public CustomResponse<RetrievePrintDTO> updatePrint(
            @RequestParam("print") String printJson,
            @RequestParam("file") MultipartFile file
    ) {
        RetrievePrintDTO dtoResponse = printServices.updatePrint(printJson, file);
        return new CustomResponse<>(true, "Print updated successfully", dtoResponse);
    }

    // Delete print type by ID
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{printId}")
    public CustomResponse<DeletePrintDTO> deletePrint(@PathVariable String printId) {
        DeletePrintDTO dtoResponse = printServices.deletePrint(printId);
        return new CustomResponse<>(true, "Print deleted successfully", dtoResponse);
    }
}
