package com.devasee.product.controller;

import com.devasee.product.dto.PrintDTO;
import com.devasee.product.services.PrintServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/product/printing")

public class PrintController {

    @Autowired
    private PrintServices printServices;

    @GetMapping("/printTypes")
    public List<PrintDTO> getPrint() {
        return printServices.getPrintType();
    }

    @GetMapping("/{printId}")
    public PrintDTO getPrintById(@PathVariable int printId) {
        return printServices.getPrintById(printId);
    }
    @PostMapping("/addPrint")
    public PrintDTO savePrint(@RequestBody PrintDTO printDTO) {
        return printServices.savePrint(printDTO);
    }

    @PutMapping("/updatePrint")
    public PrintDTO updatePrint(@RequestBody PrintDTO printDTO) {
        return printServices.updatePrint(printDTO);
    }

    @DeleteMapping("/deletePrint")
    public boolean deletePrint(@RequestBody PrintDTO printDTO) {
        return printServices.deletePrint(printDTO);
    }

}

