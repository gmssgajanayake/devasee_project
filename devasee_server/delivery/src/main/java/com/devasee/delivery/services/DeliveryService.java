package com.devasee.delivery.services;

import com.devasee.delivery.dto.CreateDeliveryDTO;
import com.devasee.delivery.dto.DeleteDeliveryDTO;
import com.devasee.delivery.dto.DeliveryStatsDTO;
import com.devasee.delivery.dto.RetrieveDeliveryDTO;
import com.devasee.delivery.dto.UpdateDeliveryDTO;
import com.devasee.delivery.entity.Delivery;
import com.devasee.delivery.enums.DeliveryStatus;
import com.devasee.delivery.exception.DeliveryNotFoundException;
import com.devasee.delivery.repo.DeliveryRepository;
import com.devasee.delivery.Interfaces.UsersClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for Delivery management
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final UsersClient usersClient; // Feign client to fetch admin name

    /**
     * CREATE a new Delivery
     */
    public CreateDeliveryDTO addDelivery(CreateDeliveryDTO dto) {
        try {
            Delivery delivery = new Delivery();
            delivery.setOrderId(dto.getOrderId());
            delivery.setCustomerId(dto.getCustomerId());
            delivery.setRecipientName(dto.getRecipientName());
            delivery.setRecipientAddress(dto.getRecipientAddress());
            delivery.setTotalAmount(dto.getTotalAmount());
            delivery.setStatus(dto.getStatus() != null ? dto.getStatus() : DeliveryStatus.Pending);
            delivery.setProducts(dto.getProducts());

            deliveryRepository.save(delivery);

            return dto; // return the same DTO back
        } catch (Exception e) {
            throw new RuntimeException("Error while adding delivery: " + e.getMessage(), e);
        }
    }

    /**
     * READ Delivery by ID
     */
    public RetrieveDeliveryDTO getDeliveryById(String id) {
        try {
            Delivery delivery = deliveryRepository.findById(id)
                    .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found with ID: " + id));

            return new RetrieveDeliveryDTO(
                    delivery.getDeliveryId(),
                    delivery.getTrackingNumber(),
                    delivery.getStatus(),
                    delivery.getDeliveryPartnerName(),
                    delivery.getCreatedAt(),
                    delivery.getUpdatedAt(),
                    delivery.getConfirmedAt(),
                    delivery.getShippedAt(),
                    delivery.getDeliveredAt(),
                    delivery.getCancelledAt()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching delivery: " + e.getMessage(), e);
        }
    }

    /**
     * READ all Deliveries with paging
     */
    public Page<RetrieveDeliveryDTO> getAllDeliveries(Pageable pageable) {
        try {
            Page<Delivery> deliveries = deliveryRepository.findAll(pageable);

            return deliveries.map(delivery -> new RetrieveDeliveryDTO(
                    delivery.getDeliveryId(),
                    delivery.getTrackingNumber(),
                    delivery.getStatus(),
                    delivery.getDeliveryPartnerName(),
                    delivery.getCreatedAt(),
                    delivery.getUpdatedAt(),
                    delivery.getConfirmedAt(),
                    delivery.getShippedAt(),
                    delivery.getDeliveredAt(),
                    delivery.getCancelledAt()
            ));
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching deliveries: " + e.getMessage(), e);
        }
    }

    /**
     * UPDATE Delivery by ID
     * Adds adminName from Feign client into DTO (not stored in entity)
     */
    public UpdateDeliveryDTO updateDelivery(String id, UpdateDeliveryDTO dto) {
        try {
            Delivery existing = deliveryRepository.findById(id)
                    .orElseThrow(() -> new DeliveryNotFoundException("Cannot update. Delivery not found with ID: " + id));

            existing.setTrackingNumber(dto.getTrackingNumber());
            existing.setStatus(dto.getStatus());
            existing.setDeliveryPartnerName(dto.getDeliveryPartnerName());
            existing.setConfirmedAt(dto.getConfirmedAt());
            existing.setShippedAt(dto.getShippedAt());
            existing.setDeliveredAt(dto.getDeliveredAt());
            existing.setCancelledAt(dto.getCancelledAt());

            Delivery saved = deliveryRepository.save(existing);

            // Fetch admin name only for DTO (not entity)
            String adminName = null;
            if (dto.getAdminId() != null) {
                adminName = usersClient.getAdminNameById(dto.getAdminId());
            }

            return new UpdateDeliveryDTO(
                    saved.getDeliveryId(),
                    saved.getTrackingNumber(),
                    saved.getStatus(),
                    saved.getDeliveryPartnerName(),
                    saved.getUpdatedAt(),
                    saved.getConfirmedAt(),
                    saved.getShippedAt(),
                    saved.getDeliveredAt(),
                    saved.getCancelledAt(),
                    dto.getAdminId(),
                    adminName
            );
        } catch (Exception e) {
            throw new RuntimeException("Error while updating delivery: " + e.getMessage(), e);
        }
    }

    /**
     * DELETE Delivery by ID
     */
    public DeleteDeliveryDTO deleteDelivery(String id) {
        try {
            Delivery existing = deliveryRepository.findById(id)
                    .orElseThrow(() -> new DeliveryNotFoundException("Cannot delete. Delivery not found with ID: " + id));

            deliveryRepository.delete(existing);

            return new DeleteDeliveryDTO(existing.getDeliveryId());
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting delivery: " + e.getMessage(), e);
        }
    }

    /**
     * DELIVERY statistics (total + pending count)
     */
    public DeliveryStatsDTO calculateDeliveryStats() {
        try {
            List<Delivery> deliveries = deliveryRepository.findAll();
            int totalDeliveries = deliveries.size();
            int pendingDeliveries = (int) deliveries.stream()
                    .filter(d -> DeliveryStatus.Pending.equals(d.getStatus()))
                    .count();
            return new DeliveryStatsDTO(totalDeliveries, pendingDeliveries);
        } catch (Exception e) {
            throw new RuntimeException("Error while calculating delivery stats: " + e.getMessage(), e);
        }
    }

    /**
     * Get all possible delivery statuses
     */
    public List<String> getAllStatuses() {
        return List.of(
                DeliveryStatus.Pending.name(),
                DeliveryStatus.Confirmed.name(),
                DeliveryStatus.Shipped.name(),
                DeliveryStatus.Delivered.name(),
                DeliveryStatus.Cancelled.name()
        );
    }
}
