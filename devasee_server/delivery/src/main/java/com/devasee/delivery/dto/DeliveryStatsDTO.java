package com.devasee.delivery.dto;

public class DeliveryStatsDTO {
    private int totalDeliveries;
    private int pendingDeliveries;

    public DeliveryStatsDTO() {}

    public DeliveryStatsDTO(int totalDeliveries, int pendingDeliveries) {
        this.totalDeliveries = totalDeliveries;
        this.pendingDeliveries = pendingDeliveries;
    }

    public int getTotalDeliveries() {
        return totalDeliveries;
    }
    public void setTotalDeliveries(int totalDeliveries) {
        this.totalDeliveries = totalDeliveries;
    }

    public int getPendingDeliveries() {
        return pendingDeliveries;
    }
    public void setPendingDeliveries(int pendingDeliveries) {
        this.pendingDeliveries = pendingDeliveries;
    }
}
