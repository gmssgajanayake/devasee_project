package com.devasee.product.repo;

import com.devasee.product.entity.BookGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookGenreRepo extends JpaRepository<BookGenre, Long> {
    Optional<BookGenre> findByNameIgnoreCase(String genre);
}
