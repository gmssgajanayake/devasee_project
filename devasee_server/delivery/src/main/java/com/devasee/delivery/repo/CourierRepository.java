package com.devasee.delivery.repo;

import com.devasee.delivery.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourierRepository extends JpaRepository<Courier, String> {
    boolean existsByName(String name);
    Optional<Courier> findByNameIgnoreCase(String name);

}
