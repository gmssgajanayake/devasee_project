package com.devasee.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private int orderId;
    private String customerName;
    private double totalPrice; // Make sure this matches OrderService field
}
