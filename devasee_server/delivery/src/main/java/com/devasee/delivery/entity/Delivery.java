package com.devasee.delivery.entity;

import com.devasee.delivery.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Delivery {

    // Unique ID for each delivery (UUID)
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false, length = 36, unique = true)
    private String deliveryId;

    // Related order ID from Order Service
    @Column(nullable = false)
    private String orderId;

    // Related customer ID from User Service
    @Column(nullable = false)
    private String customerId;

    // Unique tracking number provided by delivery partner
    @Column(name = "tracking_number", unique = true, length = 50)
    private String trackingNumber;

    // Name of the delivery recipient (end customer)
    @Column(name = "recipient_name", nullable = false, length = 100)
    private String recipientName;

    // Address where the delivery should be made
    @Column(name = "recipient_address", nullable = false, length = 200)
    private String recipientAddress;

    // Total amount of the order (useful for COD or billing info)
    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    // Current delivery status (enum: CREATED, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    // Name of the delivery partner (e.g., FedEx, DHL, LocalCourier)
    @Column(name = "delivery_partner_name", length = 100)
    private String deliveryPartnerName;

    // Products in this delivery (productId -> quantity mapping)
    @ElementCollection
    @CollectionTable(
            name = "delivery_products",
            joinColumns = @JoinColumn(name = "delivery_id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<String, Integer> products = new HashMap<>();

    // Timestamps for lifecycle tracking
    private LocalDateTime createdAt;     // When the delivery record is created
    private LocalDateTime updatedAt;     // Last time the delivery record was updated
    private LocalDateTime confirmedAt;   // When the order was confirmed
    private LocalDateTime shippedAt;     // When the package was shipped
    private LocalDateTime deliveredAt;   // When the package was delivered
    private LocalDateTime cancelledAt;   // When the delivery was cancelled

    // Auto set timestamps
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
