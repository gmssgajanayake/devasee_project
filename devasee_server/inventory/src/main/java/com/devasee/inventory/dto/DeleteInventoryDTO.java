package com.devasee.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteInventoryDTO {
    private String id;
    private String productId;  // "Deleted successfully"
}
