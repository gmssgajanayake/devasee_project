package com.devasee.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDTO {
    private String productId;
    private Integer orderQuantity;
    private String orderAddress;
    private String orderNumber;
    private String customerName;
    private LocalDate orderDate;
    private double totalAmount;
}
