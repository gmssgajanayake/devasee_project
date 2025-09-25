package com.devasee.orders.entity;

import com.devasee.orders.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false, length = 36)
    private String id;  // Unique ID for this order item

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false, length = 150)
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private double unitPrice;

    @Column(nullable = false)
    private int orderQuantity;

    // Many items belong to one order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;
}
