package com.devasee.delivery.dto;


import com.devasee.delivery.enums.DeliveryStatus;
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
    private String recipientName;
    private String recipientAddress;
    private double totalAmount;
    private DeliveryStatus status;

    // Map of productId -> quantity
    private Map<String, Integer> products;
}
