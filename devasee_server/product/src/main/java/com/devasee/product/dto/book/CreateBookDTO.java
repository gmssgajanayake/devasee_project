package com.devasee.product.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookDTO {
    private String title;
    private String author;
    private String publisher;
    private String category;
    private List<String> genres;
    private String description;
    private String language;
    private List<String> keywords;
    private double price;
    private int initialQuantity;
    private long isbn;
}
