package com.devasee.product.services;

import com.devasee.product.dto.*;
import com.devasee.product.dto.book.*;
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
import org.modelmapper.PropertyMap;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // Skip problematic fields during initial mapping to avoid ModelMapper issues
        modelMapper.typeMap(Book.class, RetrieveBookDTO.class)
                .addMappings(mapper -> {
                    mapper.skip(RetrieveBookDTO::setGenres);
                });
        RetrieveBookDTO dto = modelMapper.map(book, RetrieveBookDTO.class);

        log.info("### Adding cover img url");
        // for cover image
        String blobName = book.getImgFileName(); // stored as filename
        if (blobName != null && !blobName.isEmpty()) {
            log.info("### main file name : {}", blobName);
            String sasUrl = azureBlobService.generateSasUrl(blobName, ContainerType.BOOK);
            dto.setImgUrl(sasUrl);
        }

        log.info("### Adding other images urls");
        // for other images
        List<String> otherImgUrls = new ArrayList<>();
        List<String> storedFileNames = book.getOtherImgFileNames();
        if (storedFileNames != null && !storedFileNames.isEmpty()) {
            for (String fileName : storedFileNames) {
                if (fileName != null && !fileName.isEmpty()) {
                    log.info("### other file names : {}", fileName);
                    String otherSasUrl = azureBlobService.generateSasUrl(fileName, ContainerType.BOOK);
                    otherImgUrls.add(otherSasUrl);
                }
            }
        }
        dto.setOtherImgUrls(otherImgUrls);

        log.info("### Adding quantity to dto");
        dto.setStockQuantity(stockQuantityGetter(dto.getId()));

        log.info("### Adding categories to dto");
        dto.setCategory(book.getCategory() != null ? book.getCategory().getName() : null);

        log.info("### Adding genres to dto");
        // Genres as List<Map<String,Object>>
        List<Map<String, Object>> genreList = book.getGenres().stream()
                .map(g -> {
                    Map<String, Object> genreMap = new HashMap<>();
                    genreMap.put("id", g.getId());
                    genreMap.put("name", g.getName());
                    return genreMap;
                })
                .toList();
        dto.setGenres(genreList.isEmpty() ? null : genreList);

        log.info("### Adding languages to dto");
        dto.setLanguage(book.getLanguage() != null ? book.getLanguage().getName() : null);

        return dto;
    }

    // Convert String attributes pass in request to relevant entity
    private Book categoryGenreLanguageAdder(Book book, String category, List<String> genres, String language){

       try {
           log.info("### handling categories");
           // Handle Category
            if (category != null && !category.isBlank()) {
                BookCategory categoryEntity = bookCategoryRepo.findByNameIgnoreCase(category)
                        .orElseGet(() -> {
                            // Create new category if not exists
                            BookCategory newCategory = new BookCategory();
                            newCategory.setName(category);
                            return bookCategoryRepo.save(newCategory);
                        });
                book.setCategory(categoryEntity);
            }

           log.info("### handling generes");
            // Handle Multiple Genre
            if (genres != null && !genres.isEmpty()) {
                List<BookGenre> genreEntityList = genres.stream()
                        .map(genreName -> bookGenreRepo.findByNameIgnoreCase(genreName)
                                .orElseGet(() -> {
                                    // Create new genre if it does not exist
                                    BookGenre newGenre = new BookGenre();
                                    newGenre.setName(genreName);
                                    return bookGenreRepo.save(newGenre);
                                })
                        ).toList();
                // Clear existing genres first
                book.getGenres().clear();
                book.getGenres().addAll(genreEntityList);
            }

           log.info("### handling languages");
            // Handle Language
            if (language != null && !language.isBlank()) {
                BookLanguage languageEntity = bookLanguageRepo.findByNameIgnoreCase(language)
                        .orElseGet(() -> {
                            // Create new category if not exists
                            BookLanguage newLanguage = new BookLanguage();
                            newLanguage.setName(language);
                            return bookLanguageRepo.save(newLanguage);
                        });
                book.setLanguage(languageEntity);
            }
            return book;

        } catch (Exception e){
            log.error("### categoryGenreLanguageAdder failed", e);
            throw new ServiceUnavailableException("Something went wrong : " + e.getMessage());
        }
    }

    // Get available product item quantity from Inventory
    private int stockQuantityGetter(String productId){
        try {
            return inventoryClient.getStockQuantity(productId);
        } catch (Exception e){
            log.error("### 1 Exception details: ", e);
            throw new InternalServerErrorException("Error fetching quantity from inventory");
        }
    }

    // Get All Books
    public Page<RetrieveBookDTO> getAllBooks(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page , size, Sort.by("title").ascending());
            Page<Book> bookPage = bookRepo.findAll(pageable);

            // Convert Book → DTO and replace blob names with SAS URLs
            Page<RetrieveBookDTO> dtoPage = bookPage.map(this::sasUrlAdderAndQuantitySetter);
            if (dtoPage.isEmpty()) {
                log.error("### 2 Exception details: {}", dtoPage.isEmpty());
                throw new ProductNotFoundException("No books found");
            }

            return dtoPage;

        } catch (DataException exception){
            log.error("### Error fetching all books", exception);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    // Get all books for content-based filtering
    public List<BookContentBasedFilteringDTO> getAllBooksForContentBasedFilter() {
        try {
            List<Book> books = bookRepo.findAll();
            List<BookContentBasedFilteringDTO> bookData = new ArrayList<>();

            for (Book book : books){
                BookContentBasedFilteringDTO dto = new BookContentBasedFilteringDTO();
                dto.setId(book.getId());
                dto.setTitle(book.getTitle());
                dto.setAuthor(book.getAuthor());
                dto.setPublisher(book.getPublisher());
                dto.setDescription(book.getDescription());
                dto.setIsbn(book.getIsbn());
                dto.setCategory(book.getCategory().getName());
                dto.setLanguage(book.getLanguage().getName());

                // FIX: Use HashMap instead of Map.of()
                List<Map<String, Object>> genreList = book.getGenres().stream()
                        .map(g -> {
                            Map<String, Object> genreMap = new HashMap<>();
                            genreMap.put("id", g.getId());
                            genreMap.put("name", g.getName());
                            return genreMap;
                        })
                        .toList();
                dto.setGenres(genreList);

                bookData.add(dto);
            }

            if(bookData.isEmpty()) {
                log.warn("### Book list is empty {}", bookData);
                throw new ProductNotFoundException("No books found");
            }

            return bookData;

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
               log.error("### Error fetching books by {}", bookPage.isEmpty());
               throw new ProductNotFoundException("No books found for " + field.toLowerCase() + " : " + value);
           }

           return bookPage.map(this::sasUrlAdderAndQuantitySetter);

       } catch (ProductNotFoundException exception){
           log.error("### 4 ",exception);
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
                log.error("### 5 {}",bookPage.isEmpty());
                throw new ProductNotFoundException("No books found for given filter criteria");
            }

            return bookPage.map(this::sasUrlAdderAndQuantitySetter);

        } catch (ProductNotFoundException exception){
            log.error("### 6 ", exception);
            throw exception;
        } catch (Exception e){
            log.error("### Error fetching books by filter {}", e.getMessage());
            throw new ServiceUnavailableException("Something went wrong in server");
        }
    }

    // Save New Book In Database
    public CreateBookDTO saveBook(
            String bookJson,
            MultipartFile file,
            List<MultipartFile> otherFiles
    ) {

        CreateBookDTO bookDTO;

        try {
            // Convert JSON string to DTO
            ObjectMapper mapper = new ObjectMapper();
            bookDTO = mapper.readValue(bookJson, CreateBookDTO.class);

            if (bookRepo.existsByIsbn(bookDTO.getIsbn())) {
                throw new ProductAlreadyExistsException("Book with ISBN : " + bookDTO.getIsbn() + " already exists");
            }
        } catch (Exception e){
            log.error("### 8 ", e);
            throw new ServiceUnavailableException("Error mapping JSON to DTO : "+ e);
        }

        try {
            // Upload the cover image to Azure Blob Storage
            String fileName = (file != null) ? azureBlobService.uploadFile(file, ContainerType.BOOK) : null;
            log.info("### main file name : {}", file);

            // Upload the other image to Azure Blob Storage
            List<String> otherFilesNames = new ArrayList<>();

            if (otherFiles != null && !otherFiles.isEmpty()) {
                for (MultipartFile f : otherFiles) {
                    if(f != null && !f.isEmpty()){
                        log.info("### other file names : {}", f);
                        String uploadedFileName = azureBlobService.uploadFile(f, ContainerType.BOOK);
                        otherFilesNames.add(uploadedFileName);
                    }
                }
            }

            Book newBook = modelMapper.map(bookDTO, Book.class);
            log.info("### save newbook : "+ newBook);

            newBook.setImgFileName(fileName); // Set uploaded saved file's name as url
            newBook.setOtherImgFileNames(otherFilesNames);

            Book updatedNewBook = categoryGenreLanguageAdder(
                    newBook,
                    bookDTO.getCategory(),
                    bookDTO.getGenres(),
                    bookDTO.getLanguage()
            );
            log.info("### save updatedNewBook : "+ updatedNewBook);

            Book savedBook = bookRepo.save(updatedNewBook);

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
    public RetrieveBookDTO updateBook(
            String bookJson,
            MultipartFile file,
            List<MultipartFile> otherFiles
    ) {

        UpdateBookDTO bookDTO;

        log.info("### Updating book...");
        try {
            // Convert JSON string to DTO
            ObjectMapper mapper = new ObjectMapper();
            bookDTO = mapper.readValue(bookJson, UpdateBookDTO.class);

            Book existingBook = bookRepo.findById(bookDTO.getId()).orElseThrow(
                    ()-> new ProductNotFoundException("Book not found"));
            log.info("### Book exists");


            log.info("### 1111 : {}", bookDTO);

//            if (bookDTO.getTitle() != null){
//                existingBook.setTitle(bookDTO.getTitle());
//            }

            // map only non-null fields from DTO into existing entity
            // modelMapper.map(bookDTO, existingBook);

            // Configure ModelMapper to skip relational fields
            if (modelMapper.getTypeMap(UpdateBookDTO.class, Book.class) == null) {
                modelMapper.typeMap(UpdateBookDTO.class, Book.class).addMappings(new PropertyMap<>() {
                    @Override
                    protected void configure() {
                        skip(destination.getCategory());
                        skip(destination.getGenres());
                        skip(destination.getLanguage());
                    }
                });
            }

            log.info("### 222 dto : {}", bookDTO);
            log.info("### 222 ent : {}", existingBook);

            // Map only simple fields (title, author, publisher, description, price, isbn)
            // category, genres, language has skipped
            modelMapper.map(bookDTO, existingBook); // copies matching properties from the DTO to the existing entity

            log.info("### 333 : {}", existingBook);

            // Cover image file updating handling
            String existingFileName = existingBook.getImgFileName();

            log.info("### cover img file : {}", file);
            if(file != null && !file.isEmpty()){
                log.info("### cover image file is not null : {}", file.getOriginalFilename());
                try {
                    // Delete existing file from Azure
                    if (existingFileName != null && !existingFileName.isEmpty()) {
                        azureBlobService.deleteFile(existingFileName, ContainerType.BOOK);
                    }

                    // Upload the new image to Azure Blob Storage
                    String fileName = azureBlobService.uploadFile(file, ContainerType.BOOK);
                    existingBook.setImgFileName(fileName); // Set uploaded image file name in DTO
                } catch (Exception exception){
                    log.error("### Error  uploading cover image {}", exception.getMessage());
                    throw new ServiceUnavailableException("Something went wrong when uploading cover image");
                }
            }

            // Other img files updating handling
            List<String> existingOtherFilesNames = existingBook.getOtherImgFileNames();
            List<String> updatedFileNames = new ArrayList<>();

            log.info("### existingOtherFilesNames : {}", existingOtherFilesNames);
            log.info("### otherFiles : {}", otherFiles);

            if(otherFiles != null && !otherFiles.isEmpty()){
                log.info("### Other image files are not null : {}", otherFiles);
                log.warn("### Existing other file names : {}", existingBook.getOtherImgFileNames());
                try{
                    for (MultipartFile newFile  : otherFiles){
                        if (newFile  != null && !newFile .isEmpty()){
                            String newFileName = newFile.getOriginalFilename();
                            log.info("### Processing file: {}", newFileName);

                            // Already exists? Keep it
                            if(existingOtherFilesNames.contains(newFileName)){
                                log.info("###  Already exists keeping it : {}", newFileName);
                                updatedFileNames.add(newFileName);
                            } else {
                                // Upload new file
                                log.info("###  New file updating : {}", newFileName);
                                String uploadedFileName = azureBlobService.uploadFile(newFile, ContainerType.BOOK);
                                updatedFileNames.add(uploadedFileName);
                            }
                        }
                    }

                    // Delete files that are no longer present
                    for (String existingFile  : existingOtherFilesNames){
                        if(!updatedFileNames.contains(existingFile)){
                            log.info("### this file not longer present in updating : {}", existingFile);
                            azureBlobService.deleteFile(existingFile, ContainerType.BOOK);
                        }
                    }
                    existingBook.setOtherImgFileNames(updatedFileNames);

                } catch (Exception ex) {
                    log.error("### Error uploading other images");
                    throw new InternalServerErrorException("Something went wrong when uploading images");
                }
            }

            Book updatedExistenceBook = categoryGenreLanguageAdder(
                    existingBook,
                    bookDTO.getCategory(),
                    bookDTO.getGenres(),
                    bookDTO.getLanguage()
            );

            log.info("### 444 : {}", updatedExistenceBook);

            try {
                Book savedBook = bookRepo.save(updatedExistenceBook);
                log.info("### Book updated successfully with ID: {}", savedBook.getId());
                return sasUrlAdderAndQuantitySetter(savedBook);
            } catch (Exception e){
                log.error("### book saving error : ", e);
                throw new ServiceUnavailableException("Error saving book : " + e.getMessage());
            }

        } catch (JsonProcessingException e) {
            log.error("### Invalid JSON : {}", e.getMessage());
            throw new BadRequestException("Invalid JSON: " + e.getMessage());
        } catch (DataAccessException  e){
            log.error("### Error updating book with ID: {}", e.getMessage());
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later : " + e.getMessage());
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
            if (book.getImgFileName() != null && !book.getImgFileName().isEmpty()) {
                azureBlobService.deleteFile(book.getImgFileName(), ContainerType.BOOK);
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

    public RetrieveBookDTO deleteImgFromAzureBlobByFileName(String fileName, String productId) {
        try {
            if(fileName != null && !fileName.isEmpty()){
                azureBlobService.deleteFile(fileName, ContainerType.BOOK);
            }

            // Delete book from database
            Book book = bookRepo.findById(productId).orElseThrow(
                    ()-> new ProductNotFoundException("Book not found with id : "+ productId));

            List<String> existingFiles = book.getOtherImgFileNames();
            existingFiles.removeIf(f-> f.equals(fileName));

            book.setOtherImgFileNames(existingFiles);
            Book updatedBook = bookRepo.save(book);

            // return updated DTO with fresh SAS URLs
            return sasUrlAdderAndQuantitySetter(updatedBook);

        } catch (Exception e){
            log.error("### 10 ", e);
            throw new ServiceUnavailableException("Something went wrong in server");
        }
    }

}
