package com.devasee.product.repo;

import com.devasee.product.entity.Printing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



    public interface PrintRepo extends JpaRepository<Printing, String> {

        boolean existsByTitleAndType(String title, String type);

        Page<Printing> findByTitleContainingIgnoreCase(String title, Pageable pageable);

        Page<Printing> findByTypeContainingIgnoreCase(String type, Pageable pageable);
    }


