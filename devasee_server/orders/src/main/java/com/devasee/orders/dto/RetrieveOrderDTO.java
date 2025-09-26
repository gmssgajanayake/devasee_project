package com.devasee.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveOrderDTO {
    private String orderId;
    private String orderNumber;
    private String customerId;
    private String recipientAddress;
    private String recipientName;
    private String recipientPhoneNumber;
    private String recipientEmailAddress;
    private double totalAmount;
    private List<OrderItemDTO> items;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
