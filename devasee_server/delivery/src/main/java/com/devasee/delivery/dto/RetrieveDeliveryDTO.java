package com.devasee.delivery.dto;

import com.devasee.delivery.enums.CourierName;
import com.devasee.delivery.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveDeliveryDTO {
    private Long id;
    private Long orderId;
    private String address;
    private DeliveryStatus status;
    private CourierName courier;
    private LocalDate deliveryDate;
}
