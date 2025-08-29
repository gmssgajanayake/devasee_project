package com.devasee.analytics.services;

import com.devasee.analytics.dto.*;
import com.devasee.analytics.fetcher.AnalyticsDataFetcher;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    private final AnalyticsDataFetcher fetcher;

    public AnalyticsService(AnalyticsDataFetcher fetcher) {
        this.fetcher = fetcher;
    }

    public FullAnalyticsReportDTO generateReport() {
//        SalesSummaryDTO sales = fetcher.fetchSalesSummary(period);
//        InventoryStatusDTO inventory = fetcher.fetchInventoryStatus();
//        DeliveryStatsDTO delivery = fetcher.fetchDeliveryStats();

        //remove bellow 3 lines when future implementation
        SalesSummaryDTO sales = new SalesSummaryDTO();
        InventoryStatusDTO inventory = new InventoryStatusDTO();
        DeliveryStatsDTO delivery = new DeliveryStatsDTO();


        return new FullAnalyticsReportDTO(sales, inventory, delivery);
    }
}
