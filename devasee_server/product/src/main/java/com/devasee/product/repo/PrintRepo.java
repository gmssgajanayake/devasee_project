package com.devasee.product.repo;

import com.devasee.product.entity.Printing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrintRepo extends JpaRepository<Printing, Integer> {

    // Check if a print with the same title & type already exists (uniqueness check)
    boolean existsByTitleAndType(String title, String type);

    // Find all by type (e.g., "Mug", "T-Shirt")
    List<Printing> findByType(String type);

    // Search by title containing keyword (case-insensitive)
    List<Printing> findByTitleContainingIgnoreCase(String keyword);

    // Filter by material (e.g., "Ceramic", "Cotton")
    List<Printing> findByMaterial(String material);

    // Get items cheaper than a certain price
    List<Printing> findByPriceLessThan(double price);

    // Get items with stock available
    List<Printing> findByStockQuantityGreaterThan(int quantity);
}
