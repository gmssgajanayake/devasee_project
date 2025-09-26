package com.devasee.product.repo;

import com.devasee.product.entity.PrintProductType;
import com.devasee.product.entity.Printing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PrintRepo extends JpaRepository<Printing, String> {

    boolean existsByTitleAndTypes(String title, PrintProductType types);


    Page<Printing> findByMaterialContainingIgnoreCase(String material, Pageable pageable);

    Page<Printing> findBySizeContainingIgnoreCase(String size, Pageable pageable);

    Page<Printing> findByColorsContainingIgnoreCase(String color, Pageable pageable);


    Page<Printing> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Printing> findByTypesContainingIgnoreCase(String types, Pageable pageable);

}


