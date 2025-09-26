package com.devasee.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePrintDTO {
    private String title;
    private String types;
    private String material;
    private String size;
    private String weight;
    private String printArtWorkSize;
    private String packaging;
    private boolean giftWrapAvailable;
    private double giftWrapPrice;
    private List<String> colors;
    private double price;
    private String imgUrl;
    private List<String> otherImages;
    private int initialQuantity;
    private String category;
    private String description;
}
