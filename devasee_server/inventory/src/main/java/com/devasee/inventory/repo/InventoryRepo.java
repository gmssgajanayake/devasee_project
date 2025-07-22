package com.devasee.inventory.repo;

import com.devasee.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InventoryRepo extends JpaRepository<Inventory, Integer> {
}
