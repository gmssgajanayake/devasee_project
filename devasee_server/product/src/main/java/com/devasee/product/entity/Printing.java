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
    @Column(updatable = false, nullable = false, length = 36, unique = true)
    private String id;

    // Basic product info
    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private PrintProductType types;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private PrintingMaterial material;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private String weight;

    @Column(nullable = false)
    private String printArtWorkSize; // 80cm*50cm

    private String packaging;        // white box

    @Column(nullable = false)
    private boolean giftWrapAvailable;

    private double giftWrapPrice;

    @ElementCollection
    @CollectionTable(name = "printing_colors", joinColumns = @JoinColumn(name = "printing_id"))
    @Column(name = "color")
    private List<String> colors;

    @Column(nullable = false)
    private double price;

    private String imgUrl;

    @ElementCollection
    @CollectionTable(name = "printing_images", joinColumns = @JoinColumn(name = "printing_id"))
    @Column(name = "image_url")
    private List<String> otherImages;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private PrintingCategory category;

    @Lob
    @Column(nullable = false)
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
