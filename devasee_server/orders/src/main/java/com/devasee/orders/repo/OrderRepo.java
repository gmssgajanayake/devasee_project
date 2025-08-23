package com.devasee.orders.repo;

import com.devasee.orders.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<OrderEntity, Integer> {

    // Find all orders for a customer by name
    List<OrderEntity> findByCustomerName(String customerName);

    // Check if an order with this number already exists
    boolean existsByOrderNumber(String orderNumber);
}
