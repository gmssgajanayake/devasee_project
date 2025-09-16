package com.devasee.product.services;

import com.devasee.product.dto.*;
import com.devasee.product.entity.Book;
import com.devasee.product.entity.BookCategory;
import com.devasee.product.entity.BookGenre;
import com.devasee.product.entity.BookLanguage;
import com.devasee.product.enums.ContainerType;
import com.devasee.product.interfaces.InventoryClient;
import com.devasee.product.repo.BookCategoryRepo;
import com.devasee.product.repo.BookGenreRepo;
import com.devasee.product.repo.BookLanguageRepo;
import com.devasee.product.repo.BookRepo;
import com.devasee.product.exception.ProductAlreadyExistsException;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.exception.ServiceUnavailableException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import org.hibernate.exception.DataException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BookServices {

    private static final Logger log = LoggerFactory.getLogger(BookServices.class);

    private final BookRepo bookRepo;
    private final ModelMapper modelMapper;
    private final AzureBlobService azureBlobService;
    private final InventoryClient inventoryClient;
    private final BookCategoryRepo bookCategoryRepo;
    private final BookGenreRepo bookGenreRepo;
    private final BookLanguageRepo bookLanguageRepo;

    public BookServices(
            BookRepo bookRepo,
            ModelMapper modelMapper,
            AzureBlobService azureBlobService,
            InventoryClient inventoryClient,
            BookCategoryRepo bookCategoryRepo,
            BookGenreRepo bookGenreRepo,
            BookLanguageRepo bookLanguageRepo
    ){
        this.bookRepo = bookRepo;
        this.modelMapper = modelMapper;
        this.azureBlobService = azureBlobService;
        this.inventoryClient = inventoryClient;
        this.bookCategoryRepo = bookCategoryRepo;
        this.bookGenreRepo = bookGenreRepo;
        this.bookLanguageRepo = bookLanguageRepo;
    }

    // Due to image original url is private generate new SAS url
    // Same time add available quantity to response from Inventory
    private RetrieveBookDTO sasUrlAdderAndQuantitySetter(Book book){

            RetrieveBookDTO dto = modelMapper.map(book, RetrieveBookDTO.class);

            String blobName = dto.getImgUrl(); // stored as filename
            if (blobName != null && !blobName.isEmpty()) {
                String sasUrl = azureBlobService.generateSasUrl(blobName, ContainerType.BOOK);
                dto.setImgUrl(sasUrl);
            }

            dto.setStockQuantity(stockQuantityGetter(dto.getId()));
            dto.setCategory(book.getCategory() != null ? book.getCategory().getName() : null);
            dto.setGenre(book.getGenre() != null ? book.getGenre().getName() : null);
            dto.setLanguage(book.getLanguage() != null ? book.getLanguage().getName() : null);

        return dto;
    }

    // Get available product item quantity from Inventory
    private int stockQuantityGetter(String productId){
        try {
            return inventoryClient.getStockQuantity(productId);
        } catch (Exception e){
            throw new InternalServerErrorException("Error fetching quantity from inventory");
        }
    }

    // Get All Books
    public Page<RetrieveBookDTO> getAllBooks(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page , size, Sort.by("title").ascending());
            Page<Book> bookPage = bookRepo.findAll(pageable);

            // Convert Book → DTO and replace blob names with SAS URLs
            // Convert Book → DTO and replace blob names with SAS URLs
            Page<RetrieveBookDTO> dtoPage = bookPage.map(this::sasUrlAdderAndQuantitySetter);
            if (dtoPage.isEmpty()) {
                throw new ProductNotFoundException("No books found");
            }

            return dtoPage;

        } catch (DataException exception){
            log.error("### Error fetching all books", exception);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    // Get Book By BookId
    public RetrieveBookDTO getBookById(String bookId) {
        try {
            Book book = bookRepo.findById(bookId)
                    .orElseThrow(() -> new ProductNotFoundException("Book not found with ID: " + bookId));

           return sasUrlAdderAndQuantitySetter(book);

        } catch (DataAccessException e) {
            log.error("### Error fetching book by ID: {}", bookId, e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    // Search A Book By Title,Author, Category
    public Page<RetrieveBookDTO> searchBookByTerm(
            String field,
            String value,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

       try {
           Page<Book> bookPage = switch (field.toLowerCase()) {
               case "title" -> bookRepo.findByTitleContainingIgnoreCase(value, pageable);
               case "author" -> bookRepo.findByAuthorContainingIgnoreCase(value, pageable);
               case "isbn" -> bookRepo.findByIsbnContainingIgnoreCase(value, pageable);
               default -> throw new IllegalArgumentException("Invalid search field: " + field);
           };

           if (bookPage.isEmpty()) {
               throw new ProductNotFoundException("No books found for " + field.toLowerCase() + " : " + value);
           }

           return bookPage.map(this::sasUrlAdderAndQuantitySetter);

       } catch (ProductNotFoundException exception){
           throw exception;
       } catch (Exception e){
            log.error("### Error fetching books by {} : {}", field, value, e);
            throw new ServiceUnavailableException("Something went wrong in server");
        }
    }

    // Filter Books By Category, Publisher
    public Page<RetrieveBookDTO> filterBookByTerm(
            String category,
            String publisher,
            String genre,
            String language,
            Double minPrice,
            Double maxPrice,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        try  {
            Specification<Book> specification = (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (category != null) {
                    predicates.add(cb.like(cb.lower(root.get("category").get("name")), "%" + category.toLowerCase() + "%"));
                }
                if (publisher != null) {
                    predicates.add(cb.like(cb.lower(root.get("publisher")), "%" + publisher.toLowerCase() + "%"));
                }
                if (genre != null) {
                    predicates.add(cb.like(cb.lower(root.get("genre").get("name")), "%" + genre.toLowerCase() + "%"));
                }
                if (language != null) {
                    predicates.add(cb.like(cb.lower(root.get("language").get("name")), "%" + language.toLowerCase() + "%"));
                }
                if (minPrice != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
                }
                if (maxPrice != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
                }

                return cb.and(predicates.toArray(new Predicate[0]));

            };

            Page<Book> bookPage = bookRepo.findAll(specification, pageable);

            if (bookPage.isEmpty()) {
                throw new ProductNotFoundException("No books found for given filter criteria");
            }

            return bookPage.map(this::sasUrlAdderAndQuantitySetter);

        } catch (ProductNotFoundException exception){
            throw exception;
        } catch (Exception e){
            log.error("### Error fetching books by filter {}", e.getMessage());
            throw new ServiceUnavailableException("Something went wrong in server");
        }
    }

    // Save New Book In Database
    public CreateBookDTO saveBook(String bookJson, MultipartFile file) {

        CreateBookDTO bookDTO;

        try {
            // Convert JSON string to DTO
            ObjectMapper mapper = new ObjectMapper();
            bookDTO = mapper.readValue(bookJson, CreateBookDTO.class);

            if (bookRepo.existsByIsbn(bookDTO.getIsbn())) {
                throw new ProductAlreadyExistsException("Book with ISBN : " + bookDTO.getIsbn() + " already exists");
            }
        } catch (Exception e){
            throw new ServiceUnavailableException("Error mapping JSON to DTO : "+ e);
        }

        try {
            // Upload the image to Azure Blob Storage
            String fileName = (file!=null)? azureBlobService.uploadFile(file, ContainerType.BOOK) : null;

            // Handle Category
            BookCategory category = null;
            if (bookDTO.getCategory() != null && !bookDTO.getCategory().isBlank()) {
                category = bookCategoryRepo.findByNameIgnoreCase(bookDTO.getCategory())
                        .orElseGet(() -> {
                            // Create new category if not exists
                            BookCategory newCategory = new BookCategory();
                            newCategory.setName(bookDTO.getCategory());
                            return bookCategoryRepo.save(newCategory);
                        });
            }

            // Handle Category
            BookGenre genre = null;
            if (bookDTO.getGenre() != null && !bookDTO.getGenre().isBlank()) {
                genre = bookGenreRepo.findByNameIgnoreCase(bookDTO.getGenre())
                        .orElseGet(() -> {
                            // Create new category if not exists
                            BookGenre newGenre = new BookGenre();
                            newGenre.setName(bookDTO.getGenre());
                            return bookGenreRepo.save(newGenre);
                        });
            }

            // Handle Category
            BookLanguage language = null;
            if (bookDTO.getLanguage() != null && !bookDTO.getLanguage().isBlank()) {
                language = bookLanguageRepo.findByNameIgnoreCase(bookDTO.getLanguage())
                        .orElseGet(() -> {
                            // Create new category if not exists
                            BookLanguage newLanguage = new BookLanguage();
                            newLanguage.setName(bookDTO.getLanguage());
                            return bookLanguageRepo.save(newLanguage);
                        });
            }

            Book newBook = modelMapper.map(bookDTO, Book.class);
            newBook.setImgUrl(fileName); // Set uploaded saved file's name as url
            newBook.setCategory(category);
            newBook.setGenre(genre);
            newBook.setLanguage(language);

            Book savedBook = bookRepo.save(newBook);

            InventoryRequestDTO inventoryRequestDTO = new InventoryRequestDTO(
                    savedBook.getId(),
                    bookDTO.getInitialQuantity()
            );
            inventoryClient.createInventory(inventoryRequestDTO);

            log.info("### Book saved successfully with ID: {}", savedBook.getId());
        } catch (Exception e){
            log.error("### Error saving book with ISBN: {}", bookDTO.getIsbn(), e);
            throw new ServiceUnavailableException("Something went wrong in server");
        }

        return bookDTO;
    }

    // Update Book Details, Cover Image Not Required
    public RetrieveBookDTO updateBook(String bookJson, MultipartFile file) {

        UpdateBookDTO bookDTO;

        try {
            // Convert JSON string to DTO
            ObjectMapper mapper = new ObjectMapper();
            bookDTO = mapper.readValue(bookJson, UpdateBookDTO.class);

            Book existingBook = bookRepo.findById(bookDTO.getId()).orElseThrow(
                    ()-> new ProductNotFoundException("Book not found"));

            String existingImgUrl = existingBook.getImgUrl();

            // map only non-null fields from DTO into existing entity
            modelMapper.map(bookDTO, existingBook);

            log.info("########## file : {}",file);
            if(file != null && !file.isEmpty()){
                log.info("############### file not null : {}",existingBook.getImgUrl());
                try {
                    // Delete existing file from Azure
                    if (existingImgUrl != null && !existingImgUrl.isEmpty()) {
                        azureBlobService.deleteFile(existingImgUrl, ContainerType.BOOK);
                    }

                    // Upload the new image to Azure Blob Storage
                    String fileName = azureBlobService.uploadFile(file, ContainerType.BOOK);
                    existingBook.setImgUrl(fileName); // Set uploaded image file name in DTO
                } catch (Exception exception){
                    throw new InternalServerErrorException("Something went wrong when uploading images");
                }
            } else {
                log.info("############### current url : {} ",existingBook.getImgUrl());
                existingBook.setImgUrl(existingImgUrl);
            }

            Book savedBook = bookRepo.save(existingBook);
            log.info("### Book updated successfully with ID: {}", savedBook.getId());

            return sasUrlAdderAndQuantitySetter(savedBook);

        }catch (JsonProcessingException e) {
            log.error("### Invalid JSON : {}", e.getMessage());
            throw new BadRequestException("Invalid JSON: " + e.getMessage());
        } catch (DataAccessException  e){
            log.error("### Error updating book with ID: {}", e.getMessage());
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    // Delete Book By bookId
    public DeleteBookDTO deleteBook(String bookId) {

        Book book = bookRepo.findById(bookId).orElseThrow(
                ()-> new ProductNotFoundException("Book not found")
        );

        try {
            inventoryClient.deleteInventory(bookId);

            // Delete file from Azure
            if (book.getImgUrl() != null && !book.getImgUrl().isEmpty()) {
                azureBlobService.deleteFile(book.getImgUrl(), ContainerType.BOOK);
            }

            // Delete book from database
            bookRepo.deleteById(bookId);

            log.info("### Book deleted successfully with ID: {}", bookId);
        } catch (Exception e){
            log.error("### Error deleting book with ID: {}", bookId, e);
            throw new ServiceUnavailableException("Something went wrong in server");
        }

        return modelMapper.map(book, DeleteBookDTO.class);
    }
}



//            Page<Book> bookPage = switch (field) {
//                case PUBLISHER -> bookRepo.findByPublisherContainingIgnoreCase(value, pageable);
//                case CATEGORY -> bookRepo.findByCategoryContainingIgnoreCase(value, pageable);
//                case GENRE -> bookRepo.findByCategoryContainingIgnoreCase(value, pageable);
//                case PRICE -> bookRepo.findByCategoryContainingIgnoreCase(value, pageable);
//                // default -> throw new IllegalArgumentException("Invalid filter field: " + value);
//            };
