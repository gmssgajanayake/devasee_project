package com.devasee.product.repo;

import com.devasee.product.entity.StationeryBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationeryBrandRepo extends JpaRepository<StationeryBrand, Long> {
    Optional<StationeryBrand> findByNameIgnoreCase(String name);
}
