package com.devasee.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating Stationery
 * ID is required
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStationeryDTO {
    private String id;
    private String title;
    private String description;
    private String category;
    private String brand;
    private double price;
}
