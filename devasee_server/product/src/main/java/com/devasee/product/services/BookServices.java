package com.devasee.product.services;

import com.devasee.product.dto.CreateBookDTO;
import com.devasee.product.dto.DeleteBookDTO;
import com.devasee.product.dto.RetrieveBookDTO;
import com.devasee.product.entity.Book;
import com.devasee.product.repo.BookRepo;
import com.devasee.product.exception.ProductAlreadyExistsException;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.exception.ServiceUnavailableException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.InternalServerErrorException;
import org.hibernate.exception.DataException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class BookServices {

    private static final Logger log = LoggerFactory.getLogger(BookServices.class);

    private final BookRepo bookRepo;
    private final ModelMapper modelMapper;
    private final AzureBlobService azureBlobService;

    public BookServices(
            BookRepo bookRepo,
            ModelMapper modelMapper,
            AzureBlobService azureBlobService
    ){
        this.bookRepo = bookRepo;
        this.modelMapper = modelMapper;
        this.azureBlobService = azureBlobService;
    }

    public List<RetrieveBookDTO> getAllBooks() {
        try {
            return modelMapper.map(bookRepo.findAll(), new TypeToken<List<RetrieveBookDTO>>() {
            }.getType());
        } catch (DataException exception){
            log.error("### Error fetching all books", exception);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    public RetrieveBookDTO getBookById(String bookId) {
        try {
            Book book = bookRepo.findById(bookId)
                    .orElseThrow(() -> new ProductNotFoundException("Book not found with ID: " + bookId));
            return modelMapper.map(book, RetrieveBookDTO.class);
        } catch (DataAccessException e) {
            log.error("### Error fetching book by ID: {}", bookId, e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    public List<RetrieveBookDTO> getBookByAuthor(String author) {
        List<Book> bookList;

        try {
            bookList = bookRepo.findByAuthor(author);
        } catch (Exception e){
            log.error("### Error fetching books by author: {}", author, e);
            throw new ServiceUnavailableException("Something went wrong in server");
        }

        if (bookList== null || bookList.isEmpty()) {
            throw new ProductNotFoundException("No books found for author: " + author);
        }

        return modelMapper.map(bookList, new TypeToken<List<RetrieveBookDTO>>() {
        }.getType());
    }

    public CreateBookDTO saveBook(String bookJson, MultipartFile file) {

        CreateBookDTO bookDTO;

        try{
            // Convert JSON string to DTO
            ObjectMapper mapper = new ObjectMapper();
            bookDTO = mapper.readValue(bookJson, CreateBookDTO.class);

            if (bookRepo.existsByIsbn(bookDTO.getIsbn())) {
                throw new ProductAlreadyExistsException("Book with ISBN : " + bookDTO.getIsbn() + " already exists");
            }
        } catch (Exception e){
            throw new InternalServerErrorException("Error mapping JSON to DTO", e);
        }

        try {
            // Upload the image to Azure Blob Storage
            String imageUrl = azureBlobService.uploadFile(file);
            bookDTO.setImgUrl(imageUrl); // Set uploaded image URL in DTO

            Book savedBook = bookRepo.save(modelMapper.map(bookDTO, Book.class));
            log.info("### Book saved successfully with ID: {}", savedBook.getId());
        } catch (Exception e){
            log.error("### Error saving book with ISBN: {}", bookDTO.getIsbn(), e);
            throw new ServiceUnavailableException("Something went wrong in server");
        }

        return bookDTO;
    }

    public RetrieveBookDTO updateBook(RetrieveBookDTO bookDTO) {
        try {
            Book existingBook = bookRepo.findById(bookDTO.getId()).orElseThrow(
                    ()-> new ProductNotFoundException("Book not found"));

            Book updatedBook = modelMapper.map(bookDTO, Book.class);
            updatedBook.setId(existingBook.getId());

            Book savedBook = bookRepo.save(updatedBook);
            log.info("### Book updated successfully with ID: {}", savedBook.getId());

            return modelMapper.map(savedBook, RetrieveBookDTO.class);

        } catch (DataAccessException  e){
            log.error("### Error updating book with ID: {}", bookDTO.getId(), e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    public DeleteBookDTO deleteBook(String id) {
        Book book = bookRepo.findById(id).orElseThrow(
                ()-> new ProductNotFoundException("Book not found")
        );

        try {
            bookRepo.deleteById(id);
            log.info("### Book deleted successfully with ID: {}", id);
        } catch (Exception e){
            log.error("### Error deleting book with ID: {}", id, e);
            throw new ServiceUnavailableException("Something went wrong in server");
        }

        return modelMapper.map(book, DeleteBookDTO.class);
    }
}
