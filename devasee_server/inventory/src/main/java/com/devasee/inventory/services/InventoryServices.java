package com.devasee.inventory.services;

import com.devasee.inventory.dto.InventoryDTO;
import com.devasee.inventory.entity.Inventory;
import com.devasee.inventory.repo.InventoryRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class InventoryServices {

    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private ModelMapper modelMapper;

    public List<InventoryDTO> getAllInventory() {
        return modelMapper.map(inventoryRepo.findAll(), new TypeToken<List<InventoryDTO>>(){}.getType());
    }

    public InventoryDTO  getInventoryById(int inventoryId) {
        return modelMapper.map(inventoryRepo.findById(inventoryId), InventoryDTO.class);
    }

    public InventoryDTO saveInventory(InventoryDTO inventoryDTO) {
        inventoryRepo.save(modelMapper.map(inventoryDTO, Inventory.class));
        return inventoryDTO;
    }

    public InventoryDTO updateInventory(InventoryDTO inventoryDTO) {
        inventoryRepo.save(modelMapper.map(inventoryDTO, Inventory.class));
        return inventoryDTO;
    }

    public boolean deleteInventory(InventoryDTO inventoryDTO) {
        inventoryRepo.delete(modelMapper.map(inventoryDTO, Inventory.class));
        return true;
    }
}
