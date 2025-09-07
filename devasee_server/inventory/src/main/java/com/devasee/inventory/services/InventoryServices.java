package com.devasee.inventory.services;

import com.devasee.inventory.dto.CreateUpdateInventoryDTO;
import com.devasee.inventory.dto.DeleteInventoryDTO;
import com.devasee.inventory.dto.RetrieveInventoryDTO;
import com.devasee.inventory.entity.Inventory;
import com.devasee.inventory.exception.InventoryNotFoundException;
import com.devasee.inventory.repo.InventoryRepo;
import jakarta.transaction.Transactional;
import org.hibernate.exception.DataException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class InventoryServices {

    private final InventoryRepo inventoryRepo;
    private final ModelMapper modelMapper;

    public InventoryServices(InventoryRepo inventoryRepo, ModelMapper modelMapper) {
        this.inventoryRepo = inventoryRepo;
        this.modelMapper = modelMapper;
    }

    public List<RetrieveInventoryDTO> getAllInventory() {
        try {
            return modelMapper.map(inventoryRepo.findAll(), new TypeToken<List<RetrieveInventoryDTO>>(){}.getType());
        } catch (DataException e) {
            throw new RuntimeException("Server error. Please try again later.");
        }
    }

    public RetrieveInventoryDTO getInventoryById(String inventoryId) {
        Inventory inventory = inventoryRepo.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found with ID: " + inventoryId));
        return modelMapper.map(inventory, RetrieveInventoryDTO.class);
    }

    // Get quantity form database if error return 0
    public int getInventoryQuantityById(String productId) {
        return inventoryRepo.findByProductId(productId).map(Inventory::getAvailableQuantity)
                .orElse(0);
    }

    public CreateUpdateInventoryDTO saveInventory(CreateUpdateInventoryDTO createUpdateInventoryDTO) {
        try {
            Inventory inventory = modelMapper.map(createUpdateInventoryDTO, Inventory.class);
            Inventory savedInventory = inventoryRepo.save(inventory);
            return modelMapper.map(savedInventory, CreateUpdateInventoryDTO.class);
        }catch (Exception e){
            System.out.println("############ saveInventory err: "+e.getMessage());
        }
        return createUpdateInventoryDTO;
    }

    public RetrieveInventoryDTO updateInventory(CreateUpdateInventoryDTO inventoryDTO) {
        Inventory existingInventory = inventoryRepo.findByProductIdForUpdate(inventoryDTO.getProductId())
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found with ID: " + inventoryDTO.getProductId()));

        int updatedQuantity = existingInventory.getAvailableQuantity() + inventoryDTO.getReservedQuantity();

        if (updatedQuantity < 0) {
            throw new IllegalArgumentException("Available quantity cannot go below zero");
        }

        existingInventory.setAvailableQuantity(updatedQuantity);

        Inventory savedInventory = inventoryRepo.save(existingInventory);
        return modelMapper.map(savedInventory, RetrieveInventoryDTO.class);
    }

    public DeleteInventoryDTO deleteInventory(String inventoryId) {
        Inventory inventory = inventoryRepo.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found with ID: " + inventoryId));
        inventoryRepo.delete(inventory);
        return modelMapper.map(inventory, DeleteInventoryDTO.class);
    }
}
