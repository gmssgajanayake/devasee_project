package com.devasee.product.repo.stationery;

import com.devasee.product.entity.stationery.StationeryBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationeryBrandRepo extends JpaRepository<StationeryBrand, Long> {
    Optional<StationeryBrand> findByNameIgnoreCase(String name);
}
