package com.devasee.inventory.repo;

import com.devasee.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryRepo extends JpaRepository<Inventory, Integer> {

    // Find all inventory entries in a specific warehouse location
    @Query(value = "SELECT * FROM inventory WHERE warehouse_location = ?1", nativeQuery = true)
    List<Inventory> findByWarehouseLocation(String warehouseLocation);

    // Check if inventory exists for a particular product ID
    boolean existsByProductId(long productId);

    // Optionally find inventory by product ID
    List<Inventory> findByProductId(long productId);
}
