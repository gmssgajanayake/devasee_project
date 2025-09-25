package com.devasee.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDTO {
    private String orderNumber;
    private String customerId;
    private String recipientAddress;
    private String recipientName;
    private String recipientPhoneNumber;
    private String recipientEmailAddress;
    private double totalAmount;
    private List<OrderItemDTO> items;
}
