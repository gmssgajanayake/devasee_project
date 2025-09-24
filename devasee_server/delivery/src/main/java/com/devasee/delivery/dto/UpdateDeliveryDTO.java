package com.devasee.delivery.dto;

import com.devasee.delivery.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDeliveryDTO {
    private String deliveryId;
    private String trackingNumber;
    private DeliveryStatus status;
    private String deliveryPartnerName;
    private LocalDateTime updatedAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;
    //admin name
}
