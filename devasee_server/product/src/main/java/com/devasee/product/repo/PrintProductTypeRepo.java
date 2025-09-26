package com.devasee.product.repo;

import com.devasee.product.entity.PrintProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrintProductTypeRepo extends JpaRepository<PrintProductType, Long> {

    Optional<PrintProductType> findByName(String name);

}
