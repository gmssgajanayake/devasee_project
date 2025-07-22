package com.devasee.orders.repo;

import com.devasee.orders.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepo extends JpaRepository<OrderEntity, Integer> {
}
