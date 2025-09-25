package com.devasee.orders.dto;

import com.devasee.orders.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDeliveryDTO {

    private String orderId;
    private String customerId;
    private double totalAmount;
    private String recipientAddress;
    private String recipientName;

    // key = productId, value = quantity
    private Map<String, Integer> products;

    private DeliveryStatus status;
}
