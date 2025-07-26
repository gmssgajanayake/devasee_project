package com.devasee.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// For creation
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStationeryDTO {
    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private String imgUrl;
}
