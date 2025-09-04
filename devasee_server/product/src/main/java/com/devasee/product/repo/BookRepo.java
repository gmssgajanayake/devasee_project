package com.devasee.product.repo;

import com.devasee.product.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface BookRepo extends JpaRepository<Book, String> {

    Page<Book> findByAuthor(String author, Pageable pageable);

    // existsByIsbn(String isbn) is automatically implemented by Spring Data JPA, thanks to method name conventions.
    // Spring Data JPA provides "query method derivation".
    //This means if you define a method in your repository interface that follows a specific naming pattern like existsByFieldName
    // Look at the method name, Generate the appropriate SQL (like SELECT COUNT(*) > 0 FROM book WHERE isbn = ?)
    boolean existsByIsbn(long isbn);
}
