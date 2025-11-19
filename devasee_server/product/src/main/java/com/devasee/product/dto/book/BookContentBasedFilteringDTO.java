package com.devasee.product.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookContentBasedFilteringDTO {
    private String id;
    private String title;
    private String author;
    private String publisher;
    private String category;
    private List<Map<String, Object>> genres;
    private String description;
    private List<String> keywords;
    private String language;
    private long isbn;
}
