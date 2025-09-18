package com.devasee.delivery.dto;

import com.devasee.delivery.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeliveryDTO {
    private String orderId;
    private String address;
    private DeliveryStatus status;
    private String courierName;
    private LocalDate deliveryDate;
}
