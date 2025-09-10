package com.devasee.product.repo;

import com.devasee.product.entity.Stationery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StationeryRepo extends JpaRepository<Stationery, String> {

    boolean existsByName(String name);

    Page<Stationery> findByNameContainingIgnoreCase(String value, Pageable pageable);

}
