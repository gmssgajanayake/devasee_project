package com.devasee.inventory.controller;

import com.devasee.inventory.dto.CreateUpdateInventoryDTO;
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
    @GetMapping("/admin/allInventory")
    public CustomResponse<List<RetrieveInventoryDTO>> getAllInventory() {
        List<RetrieveInventoryDTO> inventoryList = inventoryServices.getAllInventory();
        return new CustomResponse<>(true, "Inventory found", inventoryList);
    }

    // Get inventory by id
    @GetMapping("/admin/inventoryId/{inventoryId}")
    public CustomResponse<RetrieveInventoryDTO> getInventoryById(@PathVariable String inventoryId) {
        RetrieveInventoryDTO inventoryDTO = inventoryServices.getInventoryById(inventoryId);
        return new CustomResponse<>(true, "Inventory found", inventoryDTO);
    }

    // For Product Service : Get item quantity by productId
    @GetMapping("/admin/productId/{productId}")
    public Integer getInventoryQuantityById(@PathVariable String productId) {
        return inventoryServices.getInventoryQuantityById(productId);
    }

    // For Product Service: Save inventory
    @PostMapping("/admin/addInventory")
    public CustomResponse<CreateUpdateInventoryDTO> saveInventory(@RequestBody CreateUpdateInventoryDTO inventoryDTO) {
        CreateUpdateInventoryDTO responseDTO = inventoryServices.saveInventory(inventoryDTO);
        return new CustomResponse<>(true, "Inventory saved successfully", responseDTO);
    }

    // Update inventory
    @PutMapping("/admin/updateInventory")
    public CustomResponse<RetrieveInventoryDTO> updateInventory(
            @RequestBody CreateUpdateInventoryDTO createUpdateInventoryDTO
    ) {
        RetrieveInventoryDTO updatedInventory = inventoryServices.updateInventory(createUpdateInventoryDTO);
        return new CustomResponse<>(true, "Inventory updated successfully", updatedInventory);
    }

    // Delete inventory by id
    @DeleteMapping("/admin/deleteId/{id}")
    public CustomResponse<DeleteInventoryDTO> deleteInventory(@PathVariable String id) {
        DeleteInventoryDTO deletedDTO = inventoryServices.deleteInventory(id);
        return new CustomResponse<>(true, "Inventory deleted successfully", deletedDTO);
    }

    // For Product Service : Delete inventory by deleteBookId
    @DeleteMapping("/admin/deleteByProductId/{productId}")
    public CustomResponse<DeleteInventoryDTO> deleteInventoryByBookId(@PathVariable String productId) {
        DeleteInventoryDTO deletedDTO = inventoryServices.deleteInventoryByProductId(productId);
        return new CustomResponse<>(true, "Inventory deleted successfully", deletedDTO);
    }
}
