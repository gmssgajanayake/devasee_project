package com.devasee.product.repo;

import com.devasee.product.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BookRepo extends JpaRepository<Book, String> {

    @Query(value = "SELECT * FROM book WHERE author=?1",  nativeQuery = true)
    List<Book> findByAuthor(String author);

    // existsByIsbn(String isbn) is automatically implemented by Spring Data JPA, thanks to method name conventions.
    // Spring Data JPA provides "query method derivation".
    //This means if you define a method in your repository interface that follows a specific naming pattern like existsByFieldName
    // Look at the method name, Generate the appropriate SQL (like SELECT COUNT(*) > 0 FROM book WHERE isbn = ?)
    boolean existsByIsbn(long isbn);
}
