package com.devasee.product.dto.stationery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new Stationery
 * Includes initialQuantity to create Inventory
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStationeryDTO {
    private String title;
    private String description;
    private String category;
    private String brand;
    private double price;
    private int initialQuantity; // used for inventory creation
}
