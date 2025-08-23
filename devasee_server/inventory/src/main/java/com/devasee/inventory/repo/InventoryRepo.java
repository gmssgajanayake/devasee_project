package com.devasee.inventory.repo;

import com.devasee.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryRepo extends JpaRepository<Inventory, String> {

    @Query(value = "SELECT* FROM inventory WHERE id=?1",nativeQuery = true)
    Inventory getInventoryById(String inventoryId);
}
