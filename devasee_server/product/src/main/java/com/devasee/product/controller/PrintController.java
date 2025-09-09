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

    // --------------------- Public ---------------------

    @GetMapping("/public/allPrints")
    public CustomResponse<List<PrintDTO>> getAllPrints() {
        return new CustomResponse<>(true, "Print types found", printServices.getAllPrints());
    }

    @GetMapping("/public/printId/{printId}")
    public CustomResponse<PrintDTO> getPrintById(@PathVariable int printId) {
        return new CustomResponse<>(true, "Print found", printServices.getPrintById(printId));
    }

    @GetMapping("/public/byType/{type}")
    public CustomResponse<List<PrintDTO>> getPrintsByType(@PathVariable String type) {
        return new CustomResponse<>(true, "Prints found by type", printServices.getPrintsByType(type));
    }

    @GetMapping("/public/search")
    public CustomResponse<List<PrintDTO>> searchPrints(@RequestParam String keyword) {
        return new CustomResponse<>(true, "Prints matching search", printServices.searchPrintsByTitle(keyword));
    }

    @GetMapping("/public/byMaterial/{material}")
    public CustomResponse<List<PrintDTO>> getPrintsByMaterial(@PathVariable String material) {
        return new CustomResponse<>(true, "Prints found by material", printServices.getPrintsByMaterial(material));
    }

    @GetMapping("/public/cheaperThan/{price}")
    public CustomResponse<List<PrintDTO>> getPrintsCheaperThan(@PathVariable double price) {
        return new CustomResponse<>(true, "Prints cheaper than " + price, printServices.getPrintsCheaperThan(price));
    }

    @GetMapping("/public/availableStock/{minStock}")
    public CustomResponse<List<PrintDTO>> getAvailableStock(@PathVariable int minStock) {
        return new CustomResponse<>(true, "Prints with stock greater than " + minStock, printServices.getAvailableStock(minStock));
    }

    // --------------------- Admin ---------------------

    @PostMapping("/admin/addPrint")
    public CustomResponse<PrintDTO> savePrint(@RequestBody PrintDTO printDTO) {
        return new CustomResponse<>(true, "Print type saved successfully", printServices.savePrint(printDTO));
    }

    @PutMapping("/admin/updatePrint")
    public CustomResponse<PrintDTO> updatePrint(@RequestBody PrintDTO printDTO) {
        return new CustomResponse<>(true, "Print type updated successfully", printServices.updatePrint(printDTO));
    }

    @DeleteMapping("/admin/deletePrint/{id}")
    public CustomResponse<Boolean> deletePrint(@PathVariable int id) {
        boolean deleted = printServices.deletePrint(id);
        return new CustomResponse<>(deleted, deleted ? "Print type deleted successfully" : "Failed to delete print type", deleted);
    }
}
