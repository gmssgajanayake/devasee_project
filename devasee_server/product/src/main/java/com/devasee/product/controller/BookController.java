package com.devasee.product.controller;

import com.devasee.product.dto.CreateBookDTO;
import com.devasee.product.dto.DeleteBookDTO;
import com.devasee.product.dto.RetrieveBookDTO;
import com.devasee.product.response.CustomResponse;
import com.devasee.product.services.BookServices;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // --------------------------------- Public ---------------------------------

    // Get all books
    @GetMapping("/public/allBooks")
    public CustomResponse<List<RetrieveBookDTO>> getAllBooks() {
        List<RetrieveBookDTO> bookList = bookServices.getAllBooks();
        return new CustomResponse<>(true, "Books found", bookList);
    }

    // Get book by book id
    @GetMapping("/public/bookId/{bookId}")
    public CustomResponse<RetrieveBookDTO> getBookById(@PathVariable String bookId) {
        RetrieveBookDTO bookDTO = bookServices.getBookById(bookId);
        return new CustomResponse<>(true, "Book found", bookDTO);
    }

    // Get book by author
    @GetMapping("/public/author/{author}") // TODO
    public CustomResponse<List<RetrieveBookDTO>> getBookByAuthor(@PathVariable String author) {
        List<RetrieveBookDTO> books =  bookServices.getBookByAuthor(author);
        return new CustomResponse<>(true, "Books of " + author, books);
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

//    @PostMapping("/image/upload")
//    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
//        try {
//            String url = azureBlobService.uploadFile(file);
//            return ResponseEntity.ok(url);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
//        }
//    }
}
