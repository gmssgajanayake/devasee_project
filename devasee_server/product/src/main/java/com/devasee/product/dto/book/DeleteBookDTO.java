package com.devasee.product.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteBookDTO {
    private String id;
    private String title;
    private long isbn;
    private int initialQuantity;
}
