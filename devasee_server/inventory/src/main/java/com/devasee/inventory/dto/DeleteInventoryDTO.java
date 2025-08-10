package com.devasee.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteInventoryDTO {
    private int id;
    private String message;  // "Deleted successfully"
}
