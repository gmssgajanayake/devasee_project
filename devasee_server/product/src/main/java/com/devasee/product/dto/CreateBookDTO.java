package com.devasee.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookDTO {
    private String title;
    private String author;
    private String publisher;
    private String category;
    private String genre;
    private String description;
    private String language;
    private double price;
    private int initialQuantity;
    private long isbn;
}
