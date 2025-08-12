package com.devasee.orders.repo;

import com.devasee.orders.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepo extends JpaRepository<OrderEntity, Integer> {

    // Example: Find all orders by customer name (assuming such a field exists)
    @Query(value = "SELECT * FROM orders WHERE customer_name = ?1", nativeQuery = true)
    List<OrderEntity> findByCustomerName(String customerName);

    // Check if an order exists by orderNumber (assuming a field called `orderNumber` exists)
    boolean existsByOrderNumber(String orderNumber);
}
