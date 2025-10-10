package com.devasee.product.repo.book;

import com.devasee.product.entity.book.BookLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookLanguageRepo extends JpaRepository<BookLanguage, Long> {
    Optional<BookLanguage> findByNameIgnoreCase(String category);
}
