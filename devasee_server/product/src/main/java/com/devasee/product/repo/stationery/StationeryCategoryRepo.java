package com.devasee.product.repo.stationery;

import com.devasee.product.entity.stationery.StationeryCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationeryCategoryRepo extends JpaRepository<StationeryCategory, Long> {
    Optional<StationeryCategory> findByNameIgnoreCase(String name);
}
