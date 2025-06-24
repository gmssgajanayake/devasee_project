package com.devasee.product.repo;

import com.devasee.product.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepo extends JpaRepository<Book, Integer> {
}
