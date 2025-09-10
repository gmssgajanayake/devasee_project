package com.devasee.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for retrieving Stationery details
 * Includes stock from Inventory + image SAS URL
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveStationeryDTO {
    private String id;
    private String name;
    private String description;
    private double price;
    private int stockQuantity; // comes from Inventory
    private String imgUrl;     // SAS URL for image
}
