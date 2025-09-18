package com.devasee.orders.services;

import com.devasee.orders.dto.CreateOrderDTO;
import com.devasee.orders.dto.DeleteOrderDTO;
import com.devasee.orders.dto.RetrieveOrderDTO;
import com.devasee.orders.dto.UpdateOrderDTO;
import com.devasee.orders.entity.OrderEntity;
import com.devasee.orders.exception.OrderAlreadyExistsException;
import com.devasee.orders.exception.OrderNotFoundException;
import com.devasee.orders.exception.ServiceUnavailableException;
import com.devasee.orders.repo.OrderRepo;
import jakarta.transaction.Transactional;
import org.hibernate.exception.DataException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderServices {

    private static final Logger logger = LoggerFactory.getLogger(OrderServices.class);

    private final OrderRepo orderRepo;
    private final ModelMapper modelMapper;

    public OrderServices(OrderRepo orderRepo, ModelMapper modelMapper) {
        this.orderRepo = orderRepo;
        this.modelMapper = modelMapper;
    }

    // --------------------- Retrieve ---------------------

    public Page<RetrieveOrderDTO> getAllOrders(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());
            return orderRepo.findAll(pageable).map(order -> modelMapper.map(order, RetrieveOrderDTO.class));
        } catch (DataException | DataAccessException e) {
            logger.error("Database error while fetching all orders", e);
            throw new ServiceUnavailableException("Something went wrong on the server. Please try again later.");
        }
    }

    public RetrieveOrderDTO getOrderById(String orderId) {
        try {
            OrderEntity order = orderRepo.findById(orderId).orElseThrow(
                    () -> new OrderNotFoundException("Order not found with ID: " + orderId)
            );
            return modelMapper.map(order, RetrieveOrderDTO.class);
        } catch (DataAccessException e) {
            logger.error("Database error while fetching order {}", orderId, e);
            throw new ServiceUnavailableException("Unable to retrieve order at this time.");
        }
    }

    public Page<RetrieveOrderDTO> getOrdersByCustomerName(String customerName, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());
            return orderRepo.findByCustomerNameContainingIgnoreCase(customerName, pageable)
                    .map(order -> modelMapper.map(order, RetrieveOrderDTO.class));
        } catch (DataAccessException e) {
            logger.error("Database error while fetching orders for customer {}", customerName, e);
            throw new ServiceUnavailableException("Unable to retrieve customer orders at this time.");
        }
    }

    // --------------------- Create ---------------------

    public RetrieveOrderDTO saveOrder(CreateOrderDTO orderDTO) {
        try {
            if (orderRepo.existsByOrderNumber(orderDTO.getOrderNumber())) {
                throw new OrderAlreadyExistsException(
                        "Order with number: " + orderDTO.getOrderNumber() + " already exists"
                );
            }
            OrderEntity orderEntity = modelMapper.map(orderDTO, OrderEntity.class);
            OrderEntity savedEntity = orderRepo.save(orderEntity);

            logger.info("Order {} created successfully", savedEntity.getId());
            return modelMapper.map(savedEntity, RetrieveOrderDTO.class);

        } catch (DataAccessException e) {
            logger.error("Database error while saving order {}", orderDTO.getOrderNumber(), e);
            throw new ServiceUnavailableException("Unable to save order at this time.");
        }
    }

    // --------------------- Update ---------------------

    public RetrieveOrderDTO updateOrder(UpdateOrderDTO updateOrderDTO) {
        try {
            OrderEntity existingOrder = orderRepo.findById(updateOrderDTO.getId()).orElseThrow(
                    () -> new OrderNotFoundException("Order not found with ID: " + updateOrderDTO.getId())
            );

            modelMapper.map(updateOrderDTO, existingOrder); // copy changes onto entity
            OrderEntity savedOrder = orderRepo.save(existingOrder);

            logger.info("Order {} updated successfully", savedOrder.getId());
            return modelMapper.map(savedOrder, RetrieveOrderDTO.class);

        } catch (DataAccessException e) {
            logger.error("Database error while updating order {}", updateOrderDTO.getId(), e);
            throw new ServiceUnavailableException("Unable to update order at this time.");
        }
    }

    // --------------------- Delete ---------------------

    public DeleteOrderDTO deleteOrder(String id) {
        try {
            OrderEntity order = orderRepo.findById(id).orElseThrow(
                    () -> new OrderNotFoundException("Order not found with ID: " + id)
            );

            orderRepo.delete(order);

            logger.info("Order {} deleted successfully", id);
            return new DeleteOrderDTO(order.getId(), "Order deleted successfully");

        } catch (DataAccessException e) {
            logger.error("Database error while deleting order {}", id, e);
            throw new ServiceUnavailableException("Unable to delete order at this time.");
        }
    }
}
