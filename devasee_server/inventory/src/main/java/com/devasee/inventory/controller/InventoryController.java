package com.devasee.inventory.controller;

import com.devasee.inventory.dto.CreateInventoryDTO;
import com.devasee.inventory.dto.DeleteInventoryDTO;
import com.devasee.inventory.dto.RetrieveInventoryDTO;
import com.devasee.inventory.response.CustomResponse;
import com.devasee.inventory.services.InventoryServices;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/inventory")
public class InventoryController {

    private final InventoryServices inventoryServices;

    public InventoryController(InventoryServices inventoryServices) {
        this.inventoryServices = inventoryServices;
    }

    // Get all inventory
    @GetMapping("/public/allInventory")
    public CustomResponse<List<RetrieveInventoryDTO>> getAllInventory() {
        List<RetrieveInventoryDTO> inventoryList = inventoryServices.getAllInventory();
        return new CustomResponse<>(true, "Inventory found", inventoryList);
    }

    // Get inventory by id
    @GetMapping("/public/inventoryId/{inventoryId}")
    public CustomResponse<RetrieveInventoryDTO> getInventoryById(@PathVariable String inventoryId) {
        RetrieveInventoryDTO inventoryDTO = inventoryServices.getInventoryById(inventoryId);
        return new CustomResponse<>(true, "Inventory found", inventoryDTO);
    }

    // Save inventory
    @PostMapping("/admin/addInventory")
    public CustomResponse<CreateInventoryDTO> saveInventory(@RequestBody CreateInventoryDTO inventoryDTO) {
        CreateInventoryDTO responseDTO = inventoryServices.saveInventory(inventoryDTO);
        return new CustomResponse<>(true, "Inventory saved successfully", responseDTO);
    }

    // Update inventory
    @PutMapping("/updateInventory")
    public CustomResponse<RetrieveInventoryDTO> updateInventory(@RequestBody RetrieveInventoryDTO inventoryDTO) {
        RetrieveInventoryDTO updatedInventory = inventoryServices.updateInventory(inventoryDTO);
        return new CustomResponse<>(true, "Inventory updated successfully", updatedInventory);
    }

    // Delete inventory by id
    @DeleteMapping("/deleteId/{id}")
    public CustomResponse<DeleteInventoryDTO> deleteInventory(@PathVariable String id) {
        DeleteInventoryDTO deletedDTO = inventoryServices.deleteInventory(id);
        return new CustomResponse<>(true, "Inventory deleted successfully", deletedDTO);
    }
}
