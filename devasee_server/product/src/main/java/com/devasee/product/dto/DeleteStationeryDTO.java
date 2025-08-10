package com.devasee.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class DeleteStationeryDTO {
    private int id;
    private String name;
    private int stockQuantity;
}
