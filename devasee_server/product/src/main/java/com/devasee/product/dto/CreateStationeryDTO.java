package com.devasee.product.dto;

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
    private String name;
    private String description;
    private double price;
    private int initialQuantity; // used for inventory creation
}
