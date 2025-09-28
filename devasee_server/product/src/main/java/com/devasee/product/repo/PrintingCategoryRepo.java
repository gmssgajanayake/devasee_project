package com.devasee.product.repo;

import com.devasee.product.entity.PrintingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrintingCategoryRepo extends JpaRepository<PrintingCategory, Long> {

    Optional<PrintingCategory> findByName(String name);
}

