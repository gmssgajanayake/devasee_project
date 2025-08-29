package com.devasee.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatsDTO {
    private int totalDeliveries;
    private int pendingDeliveries;
}
