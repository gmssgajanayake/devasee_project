package com.devasee.product.repo;

import com.devasee.product.entity.Stationery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationeryRepo extends JpaRepository<Stationery, Integer> {
}
