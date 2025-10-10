package com.devasee.product.controller;

import com.devasee.product.dto.book.BookContentBasedFilteringDTO;
import com.devasee.product.dto.book.CreateBookDTO;
import com.devasee.product.dto.book.DeleteBookDTO;
import com.devasee.product.dto.book.RetrieveBookDTO;
import com.devasee.product.entity.book.BookCategory;
import com.devasee.product.entity.book.BookGenre;
import com.devasee.product.entity.book.BookLanguage;
import com.devasee.product.response.CustomResponse;
import com.devasee.product.services.book.BookCategoryService;
import com.devasee.product.services.book.BookGenreService;
import com.devasee.product.services.book.BookLanguageService;
import com.devasee.product.services.book.BookServices;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/product/books")
public class BookController {

    private final BookServices bookServices;
    private final BookCategoryService bookCategoryService;
    private final BookGenreService bookGenreService;
    private final BookLanguageService bookLanguageService;

    public BookController(
            BookServices bookServices,
            BookCategoryService bookCategoryService, BookGenreService bookGenreService, BookLanguageService bookLanguageService
    ) {
        this.bookServices = bookServices;
        this.bookCategoryService = bookCategoryService;
        this.bookGenreService = bookGenreService;
        this.bookLanguageService = bookLanguageService;
    }

    // --------------------------------- Public ---------------------------------

    ///  Just make sure callers know that Spring’s pages are 0-based (page=0 is first page).

    // GET /api/v1/product/books?page=0&size=20
    // Get paginated books
    @GetMapping
    public CustomResponse<Page<RetrieveBookDTO>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<RetrieveBookDTO> bookPage  = bookServices.getAllBooks(page, size);
        return new CustomResponse<>(true, "Books found", bookPage);
    }

    // Get all books for content-based filtering
    @GetMapping("/all")
    public CustomResponse<List<BookContentBasedFilteringDTO>> getAllBooksForContentBasedFilter(
    ) {
        List<BookContentBasedFilteringDTO> books  = bookServices.getAllBooksForContentBasedFilter();
        return new CustomResponse<>(true, "All Books found", books);
    }

    // Get book by bookId
    @GetMapping("/{bookId}")
    public CustomResponse<RetrieveBookDTO> getBookById(@PathVariable String bookId) {
        RetrieveBookDTO bookDTO = bookServices.getBookById(bookId);
        return new CustomResponse<>(true, "Book found", bookDTO);
    }

    // GET /search?field=author&value=J.K.%20Rowling&page=0&size=10
    // Search book by title, author, isbn
    @GetMapping("/search")
    public CustomResponse<Page<RetrieveBookDTO>> searchBookByTerm(
            @RequestParam String field,
            @RequestParam String value,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<RetrieveBookDTO> bookDTOS = bookServices.searchBookByTerm(field, value, page, size);
        return new CustomResponse<>(
                true,
                "Books by " + field.toLowerCase() + " : " + value,
                bookDTOS
        );
    }

    // GET /filter?category=Academic&page=0&size=20
    // GET /filter?category=CATEGORY&publisher=PUBLISHER&page=0&size=20
    // GET /filter?category=CATEGORY&publisher=PUBLISHER&genre=GENRE&minPrice=100&maxPrice=300&page=0&size=20
    // GET /filter?minPrice=50&maxPrice=150&page=0&size=20

    // Search book by category, publisher
    @GetMapping("/filter")
    public CustomResponse<Page<RetrieveBookDTO>> filterBookByTerm(
            @RequestParam(required = false) String category, // the column name in database
            @RequestParam(required = false) String publisher, // the column name in database
            @RequestParam(required = false) String genre, // the column name in database
            @RequestParam(required = false) String language, // the column name in database
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<RetrieveBookDTO> bookDTOS = bookServices.filterBookByTerm(
                category,
                publisher,
                genre,
                language,
                minPrice,
                maxPrice,
                page,
                size
        );
        return new CustomResponse<>(
                true,
                "Filtered Books by : " +
                        (category != null ? "category : "+category : "") + ", " +
                        (publisher != null ? "publisher : "+publisher : "") + ", " +
                        (genre != null ? "genre : "+genre : "") + ", "+
                        (genre != null ? "language : "+language : "") + ", "+
                        (minPrice != null ? "minPrice : "+minPrice : "") + ", " +
                        (maxPrice!= null ? "maxPrice : "+maxPrice : ""),
                bookDTOS
        );
    }

    // GET all categories for dropdown
    @GetMapping("/categories")
    public CustomResponse<List<BookCategory>> getAllCategories() {
        List<BookCategory> categories = bookCategoryService.getAllCategories();
        return new CustomResponse<>(true, "All categories fetched", categories);
    }
    // GET all categories for dropdown
    @GetMapping("/genres")
    public CustomResponse<List<BookGenre>> getAllGenres() {
        List<BookGenre> genres = bookGenreService.getAllGenres();
        return new CustomResponse<>(true, "All genres fetched", genres);
    }

    // GET all categories for dropdown
    @GetMapping("/languages")
    public CustomResponse<List<BookLanguage>> getAllLanguages() {
        List<BookLanguage> languages = bookLanguageService.getAllLanguages();
        return new CustomResponse<>(true, "All languages fetched", languages);
    }





    // --------------------------------- Admin ---------------------------------

    // Save the book in database
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CustomResponse<CreateBookDTO> saveBook(
            @RequestParam("book") String bookJson,
            @RequestParam("file") MultipartFile file,
            @RequestParam("otherFiles") List<MultipartFile> otherFiles
    ) {
        CreateBookDTO dtoResponse =  bookServices.saveBook(bookJson, file, otherFiles);
        return new CustomResponse<>(true, "Book saved success", dtoResponse);
    }

    // Update book details (price, quantity, ...)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public CustomResponse<RetrieveBookDTO> updateBook(
            @RequestParam("book") String bookJson,
            @RequestParam("file") MultipartFile file,
            @RequestParam("otherFiles") List<MultipartFile> otherFiles
    ) {
        RetrieveBookDTO book = bookServices.updateBook(bookJson, file, otherFiles);
        return new CustomResponse<>(true, "Book updated success", book);
    }

    // Delete book by id
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{bookId}")
    public CustomResponse<DeleteBookDTO> deleteBook(@PathVariable String bookId) {
        DeleteBookDTO bookDTO = bookServices.deleteBook(bookId);
        return new CustomResponse<>(true, "Book deleted success", bookDTO);
    }

//    // POST to create new book category
//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping("/categories")
//    public CustomResponse<BookCategory> createCategory(@RequestParam String name) {
//        BookCategory category = bookCategoryService.createCategory(name);
//        return new CustomResponse<>(true, "Category created", category);
//    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/az")
    public CustomResponse<RetrieveBookDTO> deleteOtherImgFile (
            @RequestParam("fileName") String fileName,
            @RequestParam("productId") String productId
    ){
        RetrieveBookDTO updatedBook = bookServices.deleteImgFromAzureBlobByFileName(fileName, productId);
        return new CustomResponse<>(true, "Image deleted successfully", updatedBook);
    }
}
