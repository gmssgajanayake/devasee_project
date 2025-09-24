package com.devasee.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderDTO {
    private String orderId;
    private String productId;
    private Integer orderQuantity;
    private String orderNumber;
    private String customerId;
    private double totalAmount;
}
