package com.devasee.inventory.repo;

import com.devasee.inventory.entity.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InventoryRepo extends JpaRepository<Inventory, String> {

    @Query(value = "SELECT*FROM inventory WHERE id=?1",nativeQuery = true)
    Inventory getInventoryById(String inventoryId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Inventory i WHERE i.productId = :productId")
    Optional<Inventory> findByProductIdForUpdate(String productId);

    Optional<Inventory> findByProductId(String productId);
}
