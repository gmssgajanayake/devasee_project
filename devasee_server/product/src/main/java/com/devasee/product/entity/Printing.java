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

    // Extended product details
    private String capacity;      // e.g. 350ml
    private String weight;        // e.g. 260g
    private String printArea;     // e.g. 20cm x 8.5cm
    private String packaging;     // e.g. White box
    private String features;      // e.g. Dishwasher safe, Full colour print

    // Customization & options
    private boolean customizable;     // Can upload design/photos?
    private boolean designRequired;   // Need us to design?
    private boolean giftWrapAvailable;
    private double giftWrapPrice;

    // Order info
    private String deliveryTime;  // e.g. "24–48 hours"
    private boolean bulkAvailable;
    private Double bulkDiscount;  // optional

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
