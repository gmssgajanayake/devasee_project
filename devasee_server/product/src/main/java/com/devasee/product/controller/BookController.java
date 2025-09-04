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
        Page<RetrieveBookDTO> bookPage  = bookServices.getPaginatedBooks(page, size);
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

    // Get book by author
    @GetMapping("/public/author/{author}")
    public CustomResponse<Page<RetrieveBookDTO>> getBookByAuthor(
            @PathVariable String author,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size) {
        Page<RetrieveBookDTO> bookDTOS =  bookServices.getBookByAuthor(author, page, size);
        return new CustomResponse<>(true, "Books of " + author, bookDTOS);

        // Get first page (default 0) of books by author "J.K. Rowling" with default size 10
        // GET /public/author/J.K.%20Rowling

        // Get second page (page = 1) with 5 books per page
        // GET /public/author/J.K.%20Rowling?page=1&size=5

        // Get first page with 20 books per page
        // GET /public/author/J.K.%20Rowling?page=0&size=20

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
