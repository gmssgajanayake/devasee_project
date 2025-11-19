package com.devasee.product.dto.printing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePrintDTO {
    private String id;
    private String title;
    private String types;
    private String material;
    private String size;
    private String weight;
    private String printArtWorkSize;
    private String packaging;
    private Boolean giftWrapAvailable;
    private Double giftWrapPrice;
    private List<String> colors;
    private Double price;
    private String imgUrl;
    private List<String> otherImgUrls;
    private String category;
    private String description;
}
