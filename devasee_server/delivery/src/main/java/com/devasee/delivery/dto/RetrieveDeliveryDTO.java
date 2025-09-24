package com.devasee.delivery.dto;

import com.devasee.delivery.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveDeliveryDTO {
    private String deliveryId;
    private String orderId;
    private String productId;
    private String address;
    private DeliveryStatus status;
    private Integer orderQuantity;
    private String courierName;
    private LocalDate deliveryDate;
}
