package com.devasee.product.repo;

import com.devasee.product.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookCategoryRepo extends JpaRepository<BookCategory, Long> {
    Optional<BookCategory> findByNameIgnoreCase(String name);
}
