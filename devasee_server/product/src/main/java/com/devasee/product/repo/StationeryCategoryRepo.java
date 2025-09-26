package com.devasee.product.repo;

import com.devasee.product.entity.StationeryCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationeryCategoryRepo extends JpaRepository<StationeryCategory, Long> {
    Optional<StationeryCategory> findByNameIgnoreCase(String name);
}
