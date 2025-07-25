package com.devasee.delivery.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {
    private Long id;
    private Long orderId;
    private String address;
    private String status;
    private LocalDate deliveryDate;


    }



