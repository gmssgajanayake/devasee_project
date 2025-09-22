package com.devasee.orders.controller;

import com.devasee.orders.dto.CreateOrderDTO;
import com.devasee.orders.dto.DeleteOrderDTO;
import com.devasee.orders.dto.RetrieveOrderDTO;
import com.devasee.orders.dto.UpdateOrderDTO;
import com.devasee.orders.response.CustomResponse;
import com.devasee.orders.services.OrderServices;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/orders/order")
public class  OrderController {

    private final OrderServices orderServices;

    public OrderController(OrderServices orderServices) {
        this.orderServices = orderServices;
    }

    // --------------------- Public ---------------------

    // GET /api/v1/orders/order?page=0&size=20
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public CustomResponse<Page<RetrieveOrderDTO>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<RetrieveOrderDTO> orders = orderServices.getAllOrders(page, size);
        return new CustomResponse<>(true, "Orders retrieved successfully", orders);
    }

    // GET /api/v1/orders/order/{orderId}
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{orderId}")
    public CustomResponse<RetrieveOrderDTO> getOrderById(@PathVariable String orderId) {
        RetrieveOrderDTO order = orderServices.getOrderById(orderId);
        return new CustomResponse<>(true, "Order found", order);
    }

    // GET /api/v1/orders/order/customer/{customerName}?page=0&size=10
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/customer/{customerName}")
    public CustomResponse<Page<RetrieveOrderDTO>> getOrdersByCustomer(
            @PathVariable String customerName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<RetrieveOrderDTO> orders = orderServices.getOrdersByCustomerName(customerName, page, size);
        return new CustomResponse<>(true, "Orders for " + customerName, orders);
    }

    // --------------------- Admin ---------------------

    // Save a new order
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public CustomResponse<RetrieveOrderDTO> saveOrder(@RequestBody CreateOrderDTO orderDTO) {
        RetrieveOrderDTO savedOrder = orderServices.saveOrder(orderDTO);
        return new CustomResponse<>(true, "Order saved successfully", savedOrder);
    }


    // Update an existing order
    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public CustomResponse<RetrieveOrderDTO> updateOrder(@RequestBody UpdateOrderDTO orderDTO) {
        RetrieveOrderDTO updatedOrder = orderServices.updateOrder(orderDTO);
        return new CustomResponse<>(true, "Order updated successfully", updatedOrder);
    }

    // Delete order by ID
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public CustomResponse<DeleteOrderDTO> deleteOrder(@PathVariable String id) {
        DeleteOrderDTO deletedOrder = orderServices.deleteOrder(id);
        return new CustomResponse<>(true, "Order deleted successfully", deletedOrder);
    }
}
