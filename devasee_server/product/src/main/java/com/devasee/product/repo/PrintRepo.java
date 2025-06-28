package com.devasee.product.repo;

import com.devasee.product.entity.Printing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrintRepo extends JpaRepository<Printing, Integer> {
}
