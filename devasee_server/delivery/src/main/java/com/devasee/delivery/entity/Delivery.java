package com.devasee.delivery.entity;

import com.devasee.delivery.enums.CourierName;
import com.devasee.delivery.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private String address;

    @Enumerated(EnumType.STRING) // store enum name as string in DB
    private DeliveryStatus status;

    @Enumerated(EnumType.STRING)
    private CourierName courier;

    private LocalDate deliveryDate;
}
