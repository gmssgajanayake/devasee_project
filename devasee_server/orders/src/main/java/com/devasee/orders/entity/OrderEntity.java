package com.devasee.orders.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue()
    @UuidGenerator
    @Column(updatable = false, nullable = false, length = 36)
    private String orderId;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private Integer orderQuantity;

    @Column(nullable = false, length = 255)
    private String orderAddress;

    @Column(nullable = false, unique = true, length = 50)
    private String orderNumber;

    @Column(nullable = false, length = 100)
    private String customerID;

    @Column(nullable = false)
    private double totalAmount;


    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
