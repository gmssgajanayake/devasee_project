package com.devasee.product.repo;

import com.devasee.product.entity.Stationery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationeryRepo extends JpaRepository<Stationery, String> {

    boolean existsByTitle(String title);

    // Search by title
    Page<Stationery> findByTitleContainingIgnoreCase(String value, Pageable pageable);

    // Flexible filtering (category, brand, price, etc.)
    Page<Stationery> findAll(Specification<Stationery> specification, Pageable pageable);

    Page<Stationery> findByPrice(Double price, Pageable pageable);

    Page<Stationery> findByTitleContainingIgnoreCaseOrCategory_NameIgnoreCaseOrBrand_NameIgnoreCase(String term, String term1, String term2, Pageable pageable);
}
