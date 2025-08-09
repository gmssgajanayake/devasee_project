package com.devasee.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteDeliveryDTO {
    private Long id;
    private String address;
    private String status;
}
