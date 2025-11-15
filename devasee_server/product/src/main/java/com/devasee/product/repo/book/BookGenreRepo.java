package com.devasee.product.repo.book;

import com.devasee.product.entity.book.BookGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookGenreRepo extends JpaRepository<BookGenre, Long> {
    Optional<BookGenre> findByNameIgnoreCase(String genre);
}
