package com.devasee.product.services;

import com.devasee.product.entity.BookLanguage;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.repo.BookLanguageRepo;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookLanguageService {

    private final BookLanguageRepo bookLanguageRepo;

    public List<BookLanguage> getAllLanguages() {
        try {
            return bookLanguageRepo.findAll();
        } catch (Exception exception){
            throw new ProductNotFoundException("Languages not found");
        }
    }

    public BookLanguage createLanguage(String name) {
        BookLanguage bookLanguage = new BookLanguage();
        bookLanguage.setName(name);

        try {
            return bookLanguageRepo.save(bookLanguage);
        } catch (Exception ex){
            throw new InternalServerErrorException("Something went wrong when saving language");
        }
    }

}
