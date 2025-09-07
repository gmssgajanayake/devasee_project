package com.devasee.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateInventoryDTO {
    private String productId;
    private int reservedQuantity;
}
