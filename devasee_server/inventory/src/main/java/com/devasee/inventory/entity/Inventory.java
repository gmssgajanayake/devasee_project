package com.devasee.inventory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //    @Column(nullable = false,length = 255)
    private int quantity;
    private int reservedQuantity;
    private int availableQuantity;
    private String warehouseLocation; // optional field
    private LocalDateTime lastRestockedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.lastRestockedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Optional: Utility method to recalculate available quantity.
     */
    public void updateAvailableQuantity() {
        this.availableQuantity = this.quantity - this.reservedQuantity;
    }
}
