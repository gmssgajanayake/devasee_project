package com.devasee.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generated ID
    private int id;

    private int productId;  // Link inventory to product

    private int quantity;

    private int reservedQuantity;

    private int availableQuantity;

    private String warehouseLocation; // Optional field

    private LocalDateTime lastRestockedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.lastRestockedAt == null) {
            this.lastRestockedAt = now;
        }
        updateAvailableQuantity();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        updateAvailableQuantity();
    }

    /**
     * Utility method to recalculate available quantity.
     */
    public void updateAvailableQuantity() {
        this.availableQuantity = this.quantity - this.reservedQuantity;
    }
}
