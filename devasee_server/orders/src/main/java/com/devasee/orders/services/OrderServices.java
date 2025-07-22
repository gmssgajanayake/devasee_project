package com.devasee.orders.services;

import com.devasee.orders.dto.OrderDTO;
import com.devasee.orders.entity.OrderEntity;
import com.devasee.orders.repo.OrderRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OrderServices {

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private ModelMapper modelMapper;

    public List<OrderDTO> getAllOrders() {
        return modelMapper.map(orderRepo.findAll(), new TypeToken<List<OrderDTO>>(){}.getType());
    }

    public OrderDTO  getOrderById(int orderId) {
        return modelMapper.map(orderRepo.findById(orderId), OrderDTO.class);
    }

    public OrderDTO saveOrder(OrderDTO orderDTO) {
        orderRepo.save(modelMapper.map(orderDTO, OrderEntity.class));
        return orderDTO;
    }

    public OrderDTO updateOrder(OrderDTO orderDTO) {
        orderRepo.save(modelMapper.map(orderDTO, OrderEntity.class));
        return orderDTO;
    }

    public boolean deleteOrder(OrderDTO orderDTO) {
        orderRepo.delete(modelMapper.map(orderDTO, OrderEntity.class));
        return true;
    }
}
