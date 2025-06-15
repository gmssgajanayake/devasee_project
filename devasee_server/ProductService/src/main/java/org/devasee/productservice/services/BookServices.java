package org.devasee.productservice.services;

import jakarta.transaction.Transactional;
import org.devasee.productservice.dto.BookDTO;
import org.devasee.productservice.repo.BookRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BookServices {

    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private ModelMapper modelMapper;

    public List<BookDTO> getAllBooks() {
        return modelMapper.map(bookRepo.findAll(), new TypeToken<List<BookDTO>>(){}.getType());
    }

    public String saveBook(BookDTO bookDTO) {
        return "Book Saved";
    }
}
