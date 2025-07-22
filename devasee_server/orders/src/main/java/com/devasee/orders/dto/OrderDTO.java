package com.devasee.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderDTO {

    private int orderId;
    private int customerId;
    private double totalAmount;
    private String paymentStatus;
    private String orderStatus;
    private String shippingAddress;

}
