package com.devasee.product.repo.printing;

import com.devasee.product.entity.printing.PrintingMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrintingMaterialRepo extends JpaRepository<PrintingMaterial, String> {
    Optional<PrintingMaterial> findByName(String name);

}
