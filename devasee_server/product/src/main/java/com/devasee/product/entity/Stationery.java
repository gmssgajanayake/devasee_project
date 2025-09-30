package com.devasee.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing Stationery in DB
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Stationery {

    @Id
    @GeneratedValue()
    @UuidGenerator
    @Column(updatable = false, nullable = false, length = 36)
    private String id;

    @Column(nullable = false)
    private String title;

    // stationery belongs to exactly one category
    // Each category can contain many stationeries
    @ManyToOne
    @JoinColumn(name = "category_id")
    private StationeryCategory category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private StationeryBrand brand;


    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private double price;

    private String imgFileName;

    @ElementCollection
    private List<String> otherImgFileNames = new ArrayList<>();

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
