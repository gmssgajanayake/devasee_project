package com.devasee.product.services;

import com.devasee.product.entity.BookGenre;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.repo.BookGenreRepo;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookGenreService {

    private final BookGenreRepo bookGenreRepo;

    public List<BookGenre> getAllGenres() {
        try {
            return bookGenreRepo.findAll();
        } catch (Exception exception){
            throw new ProductNotFoundException("Genres not found");
        }
    }

    public BookGenre createGenre(String name) {
        BookGenre bookGenre = new BookGenre();
        bookGenre.setName(name);

        try {
            return bookGenreRepo.save(bookGenre);
        } catch (Exception ex){
            throw new InternalServerErrorException("Something went wrong when saving genre");
        }
    }
}
