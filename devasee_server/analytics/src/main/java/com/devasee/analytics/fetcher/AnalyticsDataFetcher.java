package com.devasee.analytics.fetcher;

import com.devasee.analytics.dto.*;
import com.devasee.analytics.response.CustomResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class AnalyticsDataFetcher {

    private final WebClient.Builder webClientBuilder;

    public AnalyticsDataFetcher(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    // Fetch and aggregate Orders
//    public SalesSummaryDTO fetchSalesSummary(String period) {
//        CustomResponse<List<OrderDTO>> response = webClientBuilder.build()
//                .get()
//                .uri("http://order-service/api/v1/orders/order/allOrders")
//                .retrieve()
//                .bodyToMono(new ParameterizedTypeReference<CustomResponse<List<OrderDTO>>>() {})
//                .block();
//
//        if (response == null || response.getData() == null) {
//            return new SalesSummaryDTO(0, 0.0);
//        }
//
//        List<OrderDTO> orders = response.getData();
//        int totalOrders = orders.size();
//        double totalRevenue = orders.stream().mapToDouble(OrderDTO::getTotalPrice).sum();
//
//        return new SalesSummaryDTO(totalOrders, totalRevenue);
//    }
    //remove below 2 lines when future implementation
    public String fetchSalesSummary(String period){
        return "fetchSalesSummary";
    }
    // Fetch and aggregate Inventory
    public InventoryStatusDTO fetchInventoryStatus() {
        CustomResponse<List<InventoryDTO>> response = webClientBuilder.build()
                .get()
                .uri("http://inventory-service/api/v1/inventory/public/allInventory")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CustomResponse<List<InventoryDTO>>>() {})
                .block();

        if (response == null || response.getData() == null) {
            return new InventoryStatusDTO(0, 0);
        }

        List<InventoryDTO> inventoryList = response.getData();
        int totalProducts = inventoryList.size();
        int lowStockItems = (int) inventoryList.stream()
                .filter(item -> item.getQuantity() < 5) // threshold = 5
                .count();

        return new InventoryStatusDTO(totalProducts, lowStockItems);
    }

    // Fetch Delivery stats (assuming delivery-service provides it)
    public DeliveryStatsDTO fetchDeliveryStats() {
        return webClientBuilder.build()
                .get()
                .uri("http://delivery-service/api/v1/delivery/stats")
                .retrieve()
                .bodyToMono(DeliveryStatsDTO.class)
                .block();
    }
}
