package com.devasee.product.services;

import com.devasee.product.entity.BookCategory;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.repo.BookCategoryRepo;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCategoryService {

    private final BookCategoryRepo bookCategoryRepo;

    public List<BookCategory> getAllCategories() {
        try {
            return bookCategoryRepo.findAll();
        } catch (Exception exception){
            throw new ProductNotFoundException("Categories not found");
        }
    }

    public BookCategory createCategory(String categoryName) {
        BookCategory bookCategory = new BookCategory();
        bookCategory.setName(categoryName);

        try {
            return bookCategoryRepo.save(bookCategory);
        } catch (Exception ex){
            throw new InternalServerErrorException("Something went wrong when saving category");
        }
    }
}
