package com.devasee.product.controller;

import com.devasee.product.dto.CreateBookDTO;
import com.devasee.product.dto.DeleteBookDTO;
import com.devasee.product.dto.RetrieveBookDTO;
import com.devasee.product.entity.Book;
import com.devasee.product.response.CustomResponse;
import com.devasee.product.services.BookServices;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/product/book")
public class BookController {

    private final BookServices bookServices;

    public BookController(
            BookServices bookServices
    ) {
        this.bookServices = bookServices;
    }

    // Get all books
    @GetMapping("/allBooks")
    public CustomResponse<List<RetrieveBookDTO>> getAllBooks() {
        List<RetrieveBookDTO> bookList = bookServices.getAllBooks();
        return new CustomResponse<>(true, "Books found", bookList);
    }

    // Get book by book id
    @GetMapping("/bookId/{bookId}")
    public CustomResponse<RetrieveBookDTO> getBookById(@PathVariable String bookId) {
        RetrieveBookDTO bookDTO = bookServices.getBookById(bookId);
        return new CustomResponse<>(true, "Book found", bookDTO);
    }

    // Get book by author
    @GetMapping("/author/{author}")
    public CustomResponse<List<RetrieveBookDTO>> getBookByAuthor(@PathVariable String author) {
        List<RetrieveBookDTO> books =  bookServices.getBookByAuthor(author);
        return new CustomResponse<>(true, "Books of " + author, books);
    }

    // Save the book in database
    @PostMapping("/addBook")
    public CustomResponse<CreateBookDTO> saveBook(@RequestBody CreateBookDTO bookDTO) {
       CreateBookDTO dtoResponse =  bookServices.saveBook(bookDTO);
        return new CustomResponse<>(true, "Book saved success", dtoResponse);
    }

    // Update book details (price, quantity, ...)
    @PutMapping("/updateBook")
    public CustomResponse<RetrieveBookDTO> updateBook(@RequestBody RetrieveBookDTO bookDTO) {
        RetrieveBookDTO book = bookServices.updateBook(bookDTO);
        return new CustomResponse<>(true, "Book updated success", book);
    }

    // Delete book by id
    @DeleteMapping("/deleteId/{id}")
    public CustomResponse<DeleteBookDTO> deleteBook(@PathVariable String id) {
        DeleteBookDTO bookDTO = bookServices.deleteBook(id);
        return new CustomResponse<>(true, "Book deleted success", bookDTO);
    }
}
