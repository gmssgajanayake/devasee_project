package com.devasee.inventory.controller;

import com.devasee.inventory.dto.CreateUpdateInventoryDTO;
import com.devasee.inventory.dto.DeleteInventoryDTO;
import com.devasee.inventory.dto.RetrieveInventoryDTO;
import com.devasee.inventory.response.CustomResponse;
import com.devasee.inventory.services.InventoryServices;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public CustomResponse<List<RetrieveInventoryDTO>> getAllInventory() {
        List<RetrieveInventoryDTO> inventoryList = inventoryServices.getAllInventory();
        return new CustomResponse<>(true, "Inventory found", inventoryList);
    }

    // Get inventory by id
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{inventoryId}")
    public CustomResponse<RetrieveInventoryDTO> getInventoryById(@PathVariable String inventoryId) {
        RetrieveInventoryDTO inventoryDTO = inventoryServices.getInventoryById(inventoryId);
        return new CustomResponse<>(true, "Inventory found", inventoryDTO);
    }

    // For Product Public Service : Get item quantity by productId (since product public end point don't have internal
    // tokens feign client don't forward anything to this that is why this sends point is public)
    @GetMapping("/product/{productId}/quantity")
    public Integer getInventoryQuantityById(@PathVariable String productId) {
        return inventoryServices.getInventoryQuantityById(productId);
    }

    // For Product Service: Save inventory
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CustomResponse<CreateUpdateInventoryDTO> saveInventory(@RequestBody CreateUpdateInventoryDTO inventoryDTO) {
        CreateUpdateInventoryDTO responseDTO = inventoryServices.saveInventory(inventoryDTO);
        return new CustomResponse<>(true, "Inventory saved successfully", responseDTO);
    }

    // Update inventory
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public CustomResponse<RetrieveInventoryDTO> updateInventory(
            @RequestBody CreateUpdateInventoryDTO createUpdateInventoryDTO
    ) {
        RetrieveInventoryDTO updatedInventory = inventoryServices.updateInventory(createUpdateInventoryDTO);
        return new CustomResponse<>(true, "Inventory updated successfully", updatedInventory);
    }

    // Delete inventory by inventoryId
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{inventoryId}")
    public CustomResponse<DeleteInventoryDTO> deleteInventory(@PathVariable String inventoryId) {
        DeleteInventoryDTO deletedDTO = inventoryServices.deleteInventory(inventoryId);
        return new CustomResponse<>(true, "Inventory deleted successfully", deletedDTO);
    }

    // For Product Service : Delete inventory by productId
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/product/{productId}")
    public CustomResponse<DeleteInventoryDTO> deleteInventoryByBookId(@PathVariable String productId) {
        DeleteInventoryDTO deletedDTO = inventoryServices.deleteInventoryByProductId(productId);
        return new CustomResponse<>(true, "Inventory deleted successfully", deletedDTO);
    }
}
