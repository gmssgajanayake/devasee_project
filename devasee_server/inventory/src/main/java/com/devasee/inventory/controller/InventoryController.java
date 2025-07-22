package com.devasee.inventory.controller;

import com.devasee.inventory.dto.InventoryDTO;
import com.devasee.inventory.services.InventoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/inventory")
public class InventoryController {


    @Autowired
    private InventoryServices inventoryServices;

    @GetMapping("/allInventory")
    public List<InventoryDTO> getInventory() {
        return inventoryServices.getAllInventory();
    }

    @GetMapping("/{inventoryId}")
    public InventoryDTO getInventoryById(@PathVariable int inventoryId) {
        return inventoryServices.getInventoryById(inventoryId);
    }

    @PostMapping("/addInventory")
    public InventoryDTO saveInventory(@RequestBody InventoryDTO inventoryDTO) {
        return inventoryServices.saveInventory(inventoryDTO);
    }

    @PutMapping("/updateInventory")
    public InventoryDTO updateInventory(@RequestBody InventoryDTO inventoryDTO) {
        return inventoryServices.updateInventory(inventoryDTO);
    }

    @DeleteMapping("/deleteInventory")
    public boolean deleteInventory(@RequestBody InventoryDTO inventoryDTO) {
        return inventoryServices.deleteInventory(inventoryDTO);
    }
}
