package com.devasee.product.services;

import com.devasee.product.dto.BookDTO;
import com.devasee.product.entity.Book;
import com.devasee.product.repo.BookRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BookServices {

    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private ModelMapper modelMapper;

    public List<BookDTO> getAllBooks() {
        return modelMapper.map(bookRepo.findAll(), new TypeToken<List<BookDTO>>(){}.getType());
    }

    public BookDTO  getBookById(int bookId) {
        return modelMapper.map(bookRepo.findById(bookId), BookDTO.class);
    }

    public BookDTO saveBook(BookDTO bookDTO) {
        bookRepo.save(modelMapper.map(bookDTO, Book.class));
        return bookDTO;
    }

    public BookDTO updateBook(BookDTO bookDTO) {
        bookRepo.save(modelMapper.map(bookDTO, Book.class));
        return bookDTO;
    }

    public boolean deleteBook(BookDTO bookDTO) {
        bookRepo.delete(modelMapper.map(bookDTO, Book.class));
        return true;
    }
}
