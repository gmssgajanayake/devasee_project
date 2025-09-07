package com.devasee.product.services;

import com.devasee.product.dto.*;
import com.devasee.product.entity.Book;
import com.devasee.product.interfaces.InventoryClient;
import com.devasee.product.repo.BookRepo;
import com.devasee.product.exception.ProductAlreadyExistsException;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.exception.ServiceUnavailableException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class BookServices {

    private static final Logger log = LoggerFactory.getLogger(BookServices.class);

    private final BookRepo bookRepo;
    private final ModelMapper modelMapper;
    private final AzureBlobService azureBlobService;
    private final InventoryClient inventoryClient;

    public BookServices(
            BookRepo bookRepo,
            ModelMapper modelMapper,
            AzureBlobService azureBlobService,
            InventoryClient inventoryClient
    ){
        this.bookRepo = bookRepo;
        this.modelMapper = modelMapper;
        this.azureBlobService = azureBlobService;
        this.inventoryClient = inventoryClient;
    }

    private RetrieveBookDTO sasUrlAdderAndQuantitySetter(Book book){

            RetrieveBookDTO dto = modelMapper.map(book, RetrieveBookDTO.class);

            String blobName = dto.getImgUrl(); // stored as filename
            if (blobName != null && !blobName.isEmpty()) {
                String sasUrl = azureBlobService.generateSasUrl(blobName);
                dto.setImgUrl(sasUrl);
            }

            dto.setStockQuantity(stockQuantityGetter(dto.getId()));

            return dto;
    }

    private int stockQuantityGetter(String productId){
        try {
            return inventoryClient.getStockQuantity(productId);
        } catch (Exception e){
            throw new InternalServerErrorException("Error fetching quantity from inventory");
        }
    }

    public Page<RetrieveBookDTO> getAllBooks(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page , size, Sort.by("title").ascending());
            Page<Book> bookPage = bookRepo.findAll(pageable);

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

    public Page<RetrieveBookDTO> searchBookByTerm(
            String field,
            String value,
            int page,
            int size
    ) {
        Page<Book> bookPage;
        Pageable pageable = PageRequest.of(page, size);

       try {
           bookPage = switch (field.toLowerCase()) {
               case "title" -> bookRepo.findByTitleContainingIgnoreCase(value, pageable);
               case "author" -> bookRepo.findByAuthorContainingIgnoreCase(value, pageable);
               case "publisher" -> bookRepo.findByPublisherContainingIgnoreCase(value, pageable);
               case "category" -> bookRepo.findByCategoryContainingIgnoreCase(value, pageable);
               default -> throw new IllegalArgumentException("Invalid search field: " + field);
           };

           if (bookPage.isEmpty()) {
               throw new ProductNotFoundException("No books found for " + field + " : " + value);
           }

           return bookPage.map(this::sasUrlAdderAndQuantitySetter);

       } catch (ProductNotFoundException exception){
           throw exception;
       } catch (Exception e){
            log.error("### Error fetching books by {} : {}", field, value, e);
            throw new ServiceUnavailableException("Something went wrong in server");
        }
    }

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
            String fileName = (file!=null)? azureBlobService.uploadFile(file) : null;

            Book newBook = modelMapper.map(bookDTO, Book.class);
            newBook.setImgUrl(fileName); // Set uploaded saved file's name as url

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
                        azureBlobService.deleteFile(existingImgUrl);
                    }

                    // Upload the new image to Azure Blob Storage
                    String fileName = azureBlobService.uploadFile(file);
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

    public DeleteBookDTO deleteBook(String id) {

        Book book = bookRepo.findById(id).orElseThrow(
                ()-> new ProductNotFoundException("Book not found")
        );

        try {

            // Delete file from Azure
            if (book.getImgUrl() != null && !book.getImgUrl().isEmpty()) {
                azureBlobService.deleteFile(book.getImgUrl());
            }

            // Delete book from database
            bookRepo.deleteById(id);
            log.info("### Book deleted successfully with ID: {}", id);
        } catch (Exception e){
            log.error("### Error deleting book with ID: {}", id, e);
            throw new ServiceUnavailableException("Something went wrong in server");
        }

        return modelMapper.map(book, DeleteBookDTO.class);
    }
}
