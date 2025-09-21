package com.devasee.orders.repo;

import com.devasee.orders.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<OrderEntity, String> {

    // Paginated search by customer name (case-insensitive, partial match)
    Page<OrderEntity> findByCustomerNameContainingIgnoreCase(String customerName, Pageable pageable);

    // Check if an order with this number already exists
    boolean existsByOrderNumber(String orderNumber);
}
