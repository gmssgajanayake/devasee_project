package com.devasee.product.repo;

import com.devasee.product.entity.BookLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookLanguageRepo extends JpaRepository<BookLanguage, Long> {
    Optional<BookLanguage> findByNameIgnoreCase(String category);
}
