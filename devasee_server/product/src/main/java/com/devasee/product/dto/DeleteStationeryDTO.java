package com.devasee.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for deleting Stationery
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteStationeryDTO {
    private String id;
    private String title;
}
