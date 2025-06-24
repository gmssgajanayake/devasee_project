package com.devasee.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private int id;
    private String author;
    private String publisher;
    private String category;
    private String description;
    private String language;
    private double price;
    private int stockQuantity;
    private long isbn;
    private String imgUrl;
}
