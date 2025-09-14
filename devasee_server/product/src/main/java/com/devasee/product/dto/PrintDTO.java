package com.devasee.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrintDTO {

    private String title;
    private String type;
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

    // Customization options
    private boolean customizable;     // Can customer add photos/text?
    private boolean designRequired;   // Need us to design if no file?
    private boolean giftWrapAvailable;
    private double giftWrapPrice;

    // Order info
    private String deliveryTime;  // e.g. "24–48 hours"
    private boolean bulkAvailable;
    private Double bulkDiscount;

    // Categorization
    private String category;
    private List<String> tags;

    // Long description
    private String description;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
