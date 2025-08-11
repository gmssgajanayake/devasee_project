package com.devasee.inventory.services;

import com.devasee.inventory.dto.CreateInventoryDTO;
import com.devasee.inventory.dto.DeleteInventoryDTO;
import com.devasee.inventory.dto.RetrieveInventoryDTO;
import com.devasee.inventory.entity.Inventory;
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

    public CreateInventoryDTO saveInventory(CreateInventoryDTO createInventoryDTO) {
        try {
            Inventory inventory = modelMapper.map(createInventoryDTO, Inventory.class);
            Inventory savedInventory = inventoryRepo.save(inventory);
            return modelMapper.map(savedInventory, CreateInventoryDTO.class);
        }catch (Exception e){
            System.out.println("############ saveInventory err: "+e.getMessage());
        }
        return createInventoryDTO;
    }

    public RetrieveInventoryDTO updateInventory(RetrieveInventoryDTO retrieveInventoryDTO) {
        Inventory existingInventory = inventoryRepo.findById(retrieveInventoryDTO.getId())
                .orElseThrow(() -> new RuntimeException("Inventory not found with ID: " + retrieveInventoryDTO.getId()));

        Inventory updatedInventory = modelMapper.map(retrieveInventoryDTO, Inventory.class);
        updatedInventory.setId(existingInventory.getId());

        Inventory savedInventory = inventoryRepo.save(updatedInventory);
        return modelMapper.map(savedInventory, RetrieveInventoryDTO.class);
    }

    public DeleteInventoryDTO deleteInventory(String inventoryId) {
        Inventory inventory = inventoryRepo.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found with ID: " + inventoryId));
        inventoryRepo.delete(inventory);
        return modelMapper.map(inventory, DeleteInventoryDTO.class);
    }
}
