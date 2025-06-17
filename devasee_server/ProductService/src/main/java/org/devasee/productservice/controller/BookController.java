package org.devasee.productservice.controller;

import org.devasee.productservice.dto.BookDTO;
import org.devasee.productservice.services.BookServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/product/book")
public class BookController {

    @Autowired
    private BookServices bookServices;

    @GetMapping("/getAllBooks")
    public List<BookDTO> getBooks() {
        return bookServices.getAllBooks();
    }

    @GetMapping("/getBookById/{id}")
    public BookDTO getBookById(@PathVariable int id) {
        return bookServices.getBookById(id);
    }

    @PostMapping("/saveBook")
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
