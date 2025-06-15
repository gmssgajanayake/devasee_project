package org.devasee.productservice.repo;

import org.devasee.productservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepo extends JpaRepository<Book, Integer> {
}
