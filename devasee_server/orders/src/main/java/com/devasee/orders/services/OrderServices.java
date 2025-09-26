package com.devasee.orders.services;

import com.devasee.orders.dto.*;
import com.devasee.orders.entity.OrderEntity;
import com.devasee.orders.entity.OrderItem;
import com.devasee.orders.enums.DeliveryStatus;
import com.devasee.orders.exception.InsufficientStockException;
import com.devasee.orders.exception.OrderAlreadyExistsException;
import com.devasee.orders.exception.OrderNotFoundException;
import com.devasee.orders.exception.ServiceUnavailableException;
import com.devasee.orders.interfaces.DeliveryClient;
import com.devasee.orders.interfaces.InventoryClient;
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


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServices {

    private static final Logger logger = LoggerFactory.getLogger(OrderServices.class);

    private final OrderRepo orderRepo;
    private final ModelMapper modelMapper;
    private final InventoryClient inventoryClient;
    private final DeliveryClient deliveryClient;

    public OrderServices(OrderRepo orderRepo, ModelMapper modelMapper, InventoryClient inventoryClient,DeliveryClient deliveryClient) {
        this.orderRepo = orderRepo;
        this.modelMapper = modelMapper;
        this.inventoryClient = inventoryClient;
        this.deliveryClient = deliveryClient;
    }
// --------------------- Retrieve ---------------------

    public Page<RetrieveOrderDTO> getAllOrders(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            return orderRepo.findAll(pageable)
                    .map(order -> modelMapper.map(order, RetrieveOrderDTO.class));
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

    public Page<RetrieveOrderDTO> getOrdersByRecipientName(String recipientName, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            return orderRepo.findByRecipientNameContainingIgnoreCase(recipientName, pageable)
                    .map(order -> modelMapper.map(order, RetrieveOrderDTO.class));
        } catch (DataAccessException e) {
            logger.error("Database error while fetching orders for recipient {}", recipientName, e);
            throw new ServiceUnavailableException("Unable to retrieve recipient orders at this time.");
        }
    }


    public Page<RetrieveOrderDTO> getOrdersByCustomerId(String customerId, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            return orderRepo.findByCustomerId(customerId, pageable)
                    .map(order -> modelMapper.map(order, RetrieveOrderDTO.class));
        } catch (DataAccessException e) {
            logger.error("Database error while fetching orders for customerId {}", customerId, e);
            throw new ServiceUnavailableException("Unable to retrieve orders for customer at this time.");
        }
    }

    public List<OrderItemDTO> getOrderItems(String orderId) {
        try {
            OrderEntity order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

            return order.getItems().stream()
                    .map(item -> modelMapper.map(item, OrderItemDTO.class))
                    .collect(Collectors.toList());

        } catch (DataAccessException e) {
            logger.error("Database error while fetching items for order {}", orderId, e);
            throw new ServiceUnavailableException("Unable to retrieve order items at this time.");
        }
    }

    // --------------------- Create ---------------------

    public RetrieveOrderDTO saveOrder(CreateOrderDTO orderDTO) {
        try {
            for (OrderItemDTO item : orderDTO.getItems()) {
                int availableStock = inventoryClient.getStockQuantity(item.getProductId());
                if (availableStock < item.getOrderQuantity()) {
                    throw new InsufficientStockException("Not enough stock for product: " + item.getProductId());
                }
            }


            if (orderRepo.existsByOrderNumber(orderDTO.getOrderNumber())) {
                throw new OrderAlreadyExistsException(
                        "Order with number: " + orderDTO.getOrderNumber() + " already exists"
                );
            }
            OrderEntity orderEntity = modelMapper.map(orderDTO, OrderEntity.class);
            OrderEntity savedEntity = orderRepo.save(orderEntity);

            logger.info("Order {} created successfully", savedEntity.getOrderId());
            // Build delivery DTO
            Map<String, Integer> products = orderDTO.getItems().stream()
                    .collect(Collectors.toMap(OrderItemDTO::getProductId, OrderItemDTO::getOrderQuantity));

            try {
                CreateDeliveryDTO deliveryDTO = new CreateDeliveryDTO(
                        savedEntity.getOrderId(),
                        orderDTO.getCustomerId(),
                        orderDTO.getTotalAmount(),
                        orderDTO.getRecipientAddress(),
                        orderDTO.getRecipientName(),
                        products,
                        DeliveryStatus.PENDING
                );
                deliveryClient.createDelivery(deliveryDTO);
            } catch (Exception ex) {
                logger.error("Failed to create delivery for order {}", savedEntity.getOrderId(), ex);
            }
            return modelMapper.map(savedEntity, RetrieveOrderDTO.class);

        } catch (DataAccessException e) {
            logger.error("Database error while saving order {}", orderDTO.getOrderNumber(), e);
            throw new ServiceUnavailableException("Unable to save order at this time.");
        }
    }

    // --------------------- Update ---------------------
    public RetrieveOrderDTO updateOrder(UpdateOrderDTO updateOrderDTO) {
        try {
            OrderEntity existingOrder = orderRepo.findById(updateOrderDTO.getOrderId())
                    .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + updateOrderDTO.getOrderId()));

            if (updateOrderDTO.getRecipientAddress() != null)
                existingOrder.setRecipientAddress(updateOrderDTO.getRecipientAddress());

            if (updateOrderDTO.getRecipientName() != null)
                existingOrder.setRecipientName(updateOrderDTO.getRecipientName());

            if (updateOrderDTO.getRecipientPhoneNumber() != null)
                existingOrder.setRecipientPhoneNumber(updateOrderDTO.getRecipientPhoneNumber());

            if (updateOrderDTO.getRecipientEmailAddress() != null)
                existingOrder.setRecipientEmailAddress(updateOrderDTO.getRecipientEmailAddress());

            if (updateOrderDTO.getTotalAmount() != null)
                existingOrder.setTotalAmount(updateOrderDTO.getTotalAmount());

            if (updateOrderDTO.getOrderNumber() != null)
                existingOrder.setOrderNumber(updateOrderDTO.getOrderNumber());

            if (updateOrderDTO.getCustomerId() != null)
                existingOrder.setCustomerId(updateOrderDTO.getCustomerId());

            if (updateOrderDTO.getItems() != null && !updateOrderDTO.getItems().isEmpty()) {
                List<OrderItem> items = updateOrderDTO.getItems().stream()
                        .map(itemDTO -> {
                            OrderItem item = modelMapper.map(itemDTO, OrderItem.class);
                            item.setOrder(existingOrder);  // 🔑 maintain the parent reference
                            return item;
                        })
                        .collect(Collectors.toList());

                existingOrder.setItems(items);
            }

            OrderEntity savedOrder = orderRepo.save(existingOrder);
            logger.info("Order {} updated successfully", savedOrder.getOrderId());

            return modelMapper.map(savedOrder, RetrieveOrderDTO.class);

        } catch (DataAccessException e) {
            logger.error("Database error while updating order {}", updateOrderDTO.getOrderId(), e);
            throw new ServiceUnavailableException("Unable to update order at this time.");
        }
    }

    public RetrieveOrderDTO updateAddress(String orderId, String newAddress) {
        try {
            OrderEntity order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

            order.setRecipientAddress(newAddress);
            OrderEntity updatedOrder = orderRepo.save(order);

            return modelMapper.map(updatedOrder, RetrieveOrderDTO.class);

        } catch (DataAccessException e) {
            logger.error("Database error while updating address for order {}", orderId, e);
            throw new ServiceUnavailableException("Unable to update order address at this time.");
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
            return new DeleteOrderDTO(order.getOrderId(), "Order deleted successfully");

        } catch (DataAccessException e) {
            logger.error("Database error while deleting order {}", id, e);
            throw new ServiceUnavailableException("Unable to delete order at this time.");
        }
    }
}
