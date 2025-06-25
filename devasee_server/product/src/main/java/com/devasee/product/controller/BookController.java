package com.devasee.product.controller;

import com.devasee.product.dto.BookDTO;
import com.devasee.product.services.BookServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/product/book")
public class BookController {

    @Autowired
    private BookServices bookServices;

    @GetMapping("/allBooks")
    public List<BookDTO> getBooks() {
        return bookServices.getAllBooks();
    }

    @GetMapping("/{bookId}")
    public BookDTO getBookById(@PathVariable int bookId) {
        return bookServices.getBookById(bookId);
    }

    @PostMapping("/addBook")
    public BookDTO saveBook(@RequestBody BookDTO bookDTO) {
        return bookServices.saveBook(bookDTO);
    }

    @PutMapping("/updateBook")
    public BookDTO updateBook(@RequestBody BookDTO bookDTO) {
        return bookServices.updateBook(bookDTO);
    }

    @DeleteMapping("/deleteBook")
    public boolean deleteBook(@RequestBody BookDTO bookDTO) {
        return bookServices.deleteBook(bookDTO);
    }
}
