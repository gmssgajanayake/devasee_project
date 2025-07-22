package com.devasee.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class InventoryDTO {
    private int id;
    private int quantity;
    private int reservedQuantity;
    private int availableQuantity;
    private String warehouseLocation;
}
