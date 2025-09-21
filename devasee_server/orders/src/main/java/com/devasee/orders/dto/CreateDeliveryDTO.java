package com.devasee.orders.dto;


import com.devasee.orders.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreateDeliveryDTO {
    private String orderId;
    private String productId;
    private DeliveryStatus status;
    private Integer orderQuantity;
}
