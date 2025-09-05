package com.devasee.product.controller;

import com.devasee.product.dto.CreateBookDTO;
import com.devasee.product.dto.DeleteBookDTO;
import com.devasee.product.dto.RetrieveBookDTO;
import com.devasee.product.response.CustomResponse;
import com.devasee.product.services.BookServices;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // --------------------------------- Public ---------------------------------

    ///  Just make sure callers know that Spring’s pages are 0-based (page=0 is first page).

    // Get paginated books
    @GetMapping("/public/books")
    public CustomResponse<Page<RetrieveBookDTO>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        Page<RetrieveBookDTO> bookPage  = bookServices.getAllBooks(page, size);
        return new CustomResponse<>(true, "Books found", bookPage);

        // Example API Call
        // /public/books?page=0&size=20   → first 20 books
        // /public/books?page=1&size=20   → next 20 books
    }

    // Get book by book id
    @GetMapping("/public/bookId/{bookId}")
    public CustomResponse<RetrieveBookDTO> getBookById(@PathVariable String bookId) {
        RetrieveBookDTO bookDTO = bookServices.getBookById(bookId);
        return new CustomResponse<>(true, "Book found", bookDTO);
    }


    // GET /public/search?field=author&value=J.K.%20Rowling&page=0&size=10
    // GET /public/search?field=title&value=Harry%20Potter
    // GET /public/search?field=category&value=Fantasy
    // Search book by field
    @GetMapping("/public/search")
    public CustomResponse<Page<RetrieveBookDTO>> getBookByTerm(
            @RequestParam String field,
            @RequestParam String value,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<RetrieveBookDTO> bookDTOS = bookServices.searchBookByTerm(field, value, page, size);

        return new CustomResponse<>(
                true,
                "Books by " + field + " : " + value,
                bookDTOS
        );
    }





    // --------------------------------- Admin ---------------------------------

    // Save the book in database
    @PostMapping("/admin/addBook")
    public CustomResponse<CreateBookDTO> saveBook(
            @RequestParam("book") String bookJson,
            @RequestParam("file") MultipartFile file
    ) {
        CreateBookDTO dtoResponse =  bookServices.saveBook(bookJson, file);
        return new CustomResponse<>(true, "Book saved success", dtoResponse);
    }

    // Update book details (price, quantity, ...)
    @PutMapping("/admin/updateBook")
    public CustomResponse<RetrieveBookDTO> updateBook(@RequestBody RetrieveBookDTO bookDTO) {
        RetrieveBookDTO book = bookServices.updateBook(bookDTO);
        return new CustomResponse<>(true, "Book updated success", book);
    }

    // Delete book by id
    @DeleteMapping("/admin/deleteId/{id}")
    public CustomResponse<DeleteBookDTO> deleteBook(@PathVariable String id) {
        DeleteBookDTO bookDTO = bookServices.deleteBook(id);
        return new CustomResponse<>(true, "Book deleted success", bookDTO);
    }

}
