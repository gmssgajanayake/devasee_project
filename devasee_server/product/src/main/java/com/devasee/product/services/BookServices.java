package com.devasee.product.services;

import com.devasee.product.dto.CreateBookDTO;
import com.devasee.product.dto.DeleteBookDTO;
import com.devasee.product.dto.RetrieveBookDTO;
import com.devasee.product.entity.Book;
import com.devasee.product.repo.BookRepo;
import com.devasee.product.exception.ProductAlreadyExistsException;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.exception.ServiceUnavailableException;
import jakarta.transaction.Transactional;
import org.hibernate.exception.DataException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BookServices {

    private final BookRepo bookRepo;
    private final ModelMapper modelMapper;

    public BookServices(
            BookRepo bookRepo,
            ModelMapper modelMapper
    ){
        this.bookRepo = bookRepo;
        this.modelMapper = modelMapper;
    }

    public List<RetrieveBookDTO> getAllBooks() {
        try {
            return modelMapper.map(bookRepo.findAll(), new TypeToken<List<RetrieveBookDTO>>() {
            }.getType());
        } catch (DataException exception){
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    public RetrieveBookDTO getBookById(String bookId) {
        Book book = bookRepo.findById(bookId).orElseThrow(
                ()-> new ProductNotFoundException("Book not found with ID: " + bookId));

        // if error occur above error will return otherwise below returned
        return modelMapper.map(book, RetrieveBookDTO.class);
    }

    public List<RetrieveBookDTO> getBookByAuthor(String author) {
        List<Book> bookList = bookRepo.findByAuthor(author);

        if (bookList.isEmpty()) {
            throw new ProductNotFoundException("No books found for author: " + author);
        }
        return modelMapper.map(bookList, new TypeToken<List<RetrieveBookDTO>>() {
        }.getType());
    }

    public CreateBookDTO saveBook(CreateBookDTO bookDTO) {

        if(bookRepo.existsByIsbn(bookDTO.getIsbn())){
            throw new ProductAlreadyExistsException("Book with ISBN : " + bookDTO.getIsbn() + " already exists");
        }
        bookRepo.save(modelMapper.map(bookDTO, Book.class));
        return bookDTO;
    }

    public RetrieveBookDTO updateBook(RetrieveBookDTO bookDTO) {

        Book existingBook = bookRepo.findById(bookDTO.getId()).orElseThrow(
                ()-> new ProductNotFoundException("Book not found"));
        Book updatedBook = modelMapper.map(bookDTO, Book.class);
        updatedBook.setId(existingBook.getId());
        Book savedBook = bookRepo.save(updatedBook);

        return modelMapper.map(savedBook, RetrieveBookDTO.class);
    }

    public DeleteBookDTO deleteBook(String id) {
        Book book = bookRepo.findById(id).orElseThrow(
                ()-> new ProductNotFoundException("Book not found")
        );
        bookRepo.deleteById(id);

        return modelMapper.map(book, DeleteBookDTO.class);
    }
}
