package com.devasee.product.controller;

import com.devasee.product.dto.PrintDTO;
import com.devasee.product.response.CustomResponse;
import com.devasee.product.services.PrintServices;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/product/printing")
public class PrintController {

    private final PrintServices printServices;

    public PrintController(PrintServices printServices) {
        this.printServices = printServices;
    }

    // --------------------------------- Public ---------------------------------

    // Get all print types
    @GetMapping("/public/allPrints")
    public CustomResponse<List<PrintDTO>> getAllPrints() {
        List<PrintDTO> printList = printServices.getPrintType();
        return new CustomResponse<>(true, "Print types found", printList);
    }

    // Get print by ID
    @GetMapping("/public/printId/{printId}")
    public CustomResponse<PrintDTO> getPrintById(@PathVariable int printId) {
        PrintDTO printDTO = printServices.getPrintById(printId);
        return new CustomResponse<>(true, "Print found", printDTO);
    }


    // --------------------------------- Admin ---------------------------------

    // Save new print type
    @PostMapping("/admin/addPrint")
    public CustomResponse<PrintDTO> savePrint(@RequestBody PrintDTO printDTO) {
        PrintDTO dtoResponse = printServices.savePrint(printDTO);
        return new CustomResponse<>(true, "Print type saved successfully", dtoResponse);
    }

    // Update print details
    @PutMapping("/admin/updatePrint")
    public CustomResponse<PrintDTO> updatePrint(@RequestBody PrintDTO printDTO) {
        PrintDTO updatedPrint = printServices.updatePrint(printDTO);
        return new CustomResponse<>(true, "Print type updated successfully", updatedPrint);
    }

    @DeleteMapping("/admin/deletePrint/{id}")
    public CustomResponse<Boolean> deletePrint(@PathVariable int id) {
        boolean deleted = printServices.deletePrint(id);
        return new CustomResponse<>(deleted, deleted ? "Print type deleted successfully" : "Failed to delete print type", deleted);
    }

}
