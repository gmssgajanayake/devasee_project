package com.devasee.orders.dto;

import com.devasee.orders.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderItemDTO {
    private String productId;
    private String productName;
    private double unitPrice;
    private int orderQuantity;
    private PaymentStatus paymentStatus;
}
