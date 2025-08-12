package com.devasee.orders.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // Matches RetrieveOrderDTO.id

    @Column(nullable = false, unique = true, length = 50)
    private String orderNumber;  // Matches CreateOrderDTO.orderNumber

    @Column(nullable = false, length = 100)
    private String customerName; // Matches CreateOrderDTO.customerName

    @Column(nullable = false)
    private LocalDate orderDate; // Matches CreateOrderDTO.orderDate

    @Column(nullable = false)
    private double totalAmount;  // Matches CreateOrderDTO.totalAmount

    // Optional: Keep system tracking fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
