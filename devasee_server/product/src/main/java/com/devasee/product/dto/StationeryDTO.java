package com.devasee.product.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationeryDTO {
    private int id;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private String imgUrl;
}
