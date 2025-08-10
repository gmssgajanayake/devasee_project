package com.devasee.orders.services;

import com.devasee.orders.dto.CreateOrderDTO;
import com.devasee.orders.dto.DeleteOrderDTO;
import com.devasee.orders.dto.RetrieveOrderDTO;
import com.devasee.orders.entity.OrderEntity;
import com.devasee.orders.exception.OrderAlreadyExistsException;
import com.devasee.orders.exception.OrderNotFoundException;
import com.devasee.orders.exception.ServiceUnavailableException;
import com.devasee.orders.repo.OrderRepo;
import jakarta.transaction.Transactional;
import org.hibernate.exception.DataException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OrderServices {

    private final OrderRepo orderRepo;
    private final ModelMapper modelMapper;

    public OrderServices(OrderRepo orderRepo, ModelMapper modelMapper) {
        this.orderRepo = orderRepo;
        this.modelMapper = modelMapper;
    }

    public List<RetrieveOrderDTO> getAllOrders() {
        try {
            return modelMapper.map(orderRepo.findAll(), new TypeToken<List<RetrieveOrderDTO>>() {}.getType());
        } catch (DataException exception) {
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    public RetrieveOrderDTO getOrderById(int orderId) {
        OrderEntity order = orderRepo.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("Order not found with ID: " + orderId)
        );
        return modelMapper.map(order, RetrieveOrderDTO.class);
    }

    public List<RetrieveOrderDTO> getOrdersByCustomerName(String customerName) {
        List<OrderEntity> orderList = orderRepo.findByCustomerName(customerName);
        if (orderList.isEmpty()) {
            throw new OrderNotFoundException("No orders found for customer: " + customerName);
        }
        return modelMapper.map(orderList, new TypeToken<List<RetrieveOrderDTO>>() {}.getType());
    }

    public CreateOrderDTO saveOrder(CreateOrderDTO orderDTO) {
        if (orderRepo.existsByOrderNumber(orderDTO.getOrderNumber())) {
            throw new OrderAlreadyExistsException("Order with number: " + orderDTO.getOrderNumber() + " already exists");
        }
        orderRepo.save(modelMapper.map(orderDTO, OrderEntity.class));
        return orderDTO;
    }

    public RetrieveOrderDTO updateOrder(RetrieveOrderDTO orderDTO) {
        OrderEntity existingOrder = orderRepo.findById(orderDTO.getId()).orElseThrow(
                () -> new OrderNotFoundException("Order not found with ID: " + orderDTO.getId())
        );
        OrderEntity updatedOrder = modelMapper.map(orderDTO, OrderEntity.class);
        updatedOrder.setId(existingOrder.getId()); // Ensure ID is not changed
        OrderEntity savedOrder = orderRepo.save(updatedOrder);
        return modelMapper.map(savedOrder, RetrieveOrderDTO.class);
    }

    public DeleteOrderDTO deleteOrder(int id) {
        OrderEntity order = orderRepo.findById(id).orElseThrow(
                () -> new OrderNotFoundException("Order not found with ID: " + id)
        );
        orderRepo.deleteById(id);
        return modelMapper.map(order, DeleteOrderDTO.class);
    }
}
