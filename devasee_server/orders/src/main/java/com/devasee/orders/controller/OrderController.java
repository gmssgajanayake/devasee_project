package com.devasee.orders.controller;

import com.devasee.orders.dto.OrderDTO;
import com.devasee.orders.services.OrderServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/orders/order")
public class OrderController {

    @Autowired
    private OrderServices orderServices;

    @GetMapping("/allOrders")
    public List<OrderDTO> getOrders() {
        return orderServices.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public OrderDTO getOrderById(@PathVariable int orderId) {
        return orderServices.getOrderById(orderId);
    }

    @PostMapping("/addOrder")
    public OrderDTO saveOrder(@RequestBody OrderDTO orderDTO) {
        return orderServices.saveOrder(orderDTO);
    }

    @PutMapping("/updateOrder")
    public OrderDTO updateOrder(@RequestBody OrderDTO orderDTO) {
        return orderServices.updateOrder(orderDTO);
    }

    @DeleteMapping("/deleteOrder")
    public boolean deleteOrder(@RequestBody OrderDTO orderDTO) {
        return orderServices.deleteOrder(orderDTO);
    }

}
