package com.devasee.delivery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeliveryPartner {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false, length = 36, unique = true)
    private String id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false, length = 10)
    private String phone;

    @Column(name = "tracking_link")
    private String trackingLink;
}
