package com.devasee.delivery.dto;

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
    private String status;
    private LocalDate deliveryDate;
}
