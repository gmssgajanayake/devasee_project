package com.devasee.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveInventoryDTO {
    private String id;
    private String productId;
    private int availableQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
