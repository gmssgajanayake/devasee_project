package com.devasee.delivery.dto;


import com.devasee.delivery.enums.DeliveryStatus;
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
