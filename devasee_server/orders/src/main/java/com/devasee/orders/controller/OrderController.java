package com.devasee.orders.controller;

import com.devasee.orders.dto.CreateOrderDTO;
import com.devasee.orders.dto.DeleteOrderDTO;
import com.devasee.orders.dto.RetrieveOrderDTO;
import com.devasee.orders.response.CustomResponse;
import com.devasee.orders.services.OrderServices;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/orders/order")
public class OrderController {

    private final OrderServices orderServices;

    public OrderController(OrderServices orderServices) {
        this.orderServices = orderServices;
    }

    // Get all orders
    @GetMapping("/public/allOrders")
    public CustomResponse<List<RetrieveOrderDTO>> getAllOrders() {
        List<RetrieveOrderDTO> orders = orderServices.getAllOrders();
        return new CustomResponse<>(true, "Orders retrieved successfully", orders);
    }

    // Get order by order ID
    @GetMapping("/public/orderId/{orderId}")
    public CustomResponse<RetrieveOrderDTO> getOrderById(@PathVariable int orderId) {
        RetrieveOrderDTO order = orderServices.getOrderById(orderId);
        return new CustomResponse<>(true, "Order found", order);
    }

    // Get orders by customer name
    @GetMapping("/public/customer/{customerName}")
    public CustomResponse<List<RetrieveOrderDTO>> getOrdersByCustomer(@PathVariable String customerName) {
        List<RetrieveOrderDTO> orders = orderServices.getOrdersByCustomerName(customerName);
        return new CustomResponse<>(true, "Orders for " + customerName, orders);
    }

    // Save a new order
    @PostMapping("/addOrder")
    public CustomResponse<CreateOrderDTO> saveOrder(@RequestBody CreateOrderDTO orderDTO) {
        CreateOrderDTO savedOrder = orderServices.saveOrder(orderDTO);
        return new CustomResponse<>(true, "Order saved successfully", savedOrder);
    }

    // Update an existing order
    @PutMapping("/updateOrder")
    public CustomResponse<RetrieveOrderDTO> updateOrder(@RequestBody RetrieveOrderDTO orderDTO) {
        RetrieveOrderDTO updatedOrder = orderServices.updateOrder(orderDTO);
        return new CustomResponse<>(true, "Order updated successfully", updatedOrder);
    }

    // Delete order by ID
    @DeleteMapping("/deleteId/{id}")
    public CustomResponse<DeleteOrderDTO> deleteOrder(@PathVariable int id) {
        DeleteOrderDTO deletedOrder = orderServices.deleteOrder(id);
        return new CustomResponse<>(true, "Order deleted successfully", deletedOrder);
    }
}
