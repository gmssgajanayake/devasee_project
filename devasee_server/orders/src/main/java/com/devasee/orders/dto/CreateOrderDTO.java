package com.devasee.orders.dto;

import com.devasee.orders.enums.PaymentMethod;
import com.devasee.orders.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDTO {
    private String customerId;
    private String recipientAddress;
    private String recipientName;
    private String recipientPhoneNumber;
    private String recipientEmailAddress;
    private String city;
    private String postalCode;
    private double totalAmount;
    private List<OrderItemDTO> items;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;

}
