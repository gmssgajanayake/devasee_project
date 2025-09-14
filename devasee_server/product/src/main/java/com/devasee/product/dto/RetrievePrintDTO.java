package com.devasee.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrievePrintDTO {
    private String id;
    private String title;
    private String type;
    private String material;
    private String size;
    private String color;
    private double price;
    private int stockQuantity;
    private String imgUrl;

    // Categorization
    private String category;
    private List<String> tags;

    // Long description
    private String description;
;
}
