package com.devasee.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullAnalyticsReportDTO {
    private SalesSummaryDTO salesSummary;
    private InventoryStatusDTO inventoryStatus;
    private DeliveryStatsDTO deliveryStats;
}
