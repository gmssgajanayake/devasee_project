package com.devasee.delivery.dto;

import com.devasee.delivery.enums.CourierName;
import com.devasee.delivery.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteDeliveryDTO {
    private Long id;
    private String address;
    private DeliveryStatus status;
    private CourierName courier;
}
