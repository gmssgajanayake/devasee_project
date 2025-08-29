package com.devasee.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryStatusDTO {
    private int totalProducts;
    private int lowStockItems;
}
