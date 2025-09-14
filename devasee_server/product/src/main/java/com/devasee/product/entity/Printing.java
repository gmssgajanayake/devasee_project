package com.devasee.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Printing {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false, length = 36)
    private String id;

    // Basic product info
    private String title;
    private String type; // Mug, T-shirt, Banner etc.
    private String material;
    private String size;
    private String color;
    private double price;
    private int stockQuantity;
    private String imgUrl;


    // Categorization
    private String category;
    @ElementCollection
    private List<String> tags;

    @Lob
    private String description;

    // Timestamps
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
