package com.devasee.delivery.services;

import com.devasee.delivery.dto.CreateDeliveryDTO;
import com.devasee.delivery.dto.DeleteDeliveryDTO;
import com.devasee.delivery.dto.DeliveryStatsDTO;
import com.devasee.delivery.dto.RetrieveDeliveryDTO;
import com.devasee.delivery.entity.Delivery;
import com.devasee.delivery.enums.DeliveryStatus;
import com.devasee.delivery.exception.DeliveryNotFoundException;
import com.devasee.delivery.repo.DeliveryRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final ModelMapper modelMapper;

    public DeliveryService(DeliveryRepository deliveryRepository, ModelMapper modelMapper) {
        this.deliveryRepository = deliveryRepository;
        this.modelMapper = modelMapper;
    }

    // CREATE Delivery
    public CreateDeliveryDTO addDelivery(CreateDeliveryDTO dto) {
        try {
            Delivery delivery = modelMapper.map(dto, Delivery.class);
            delivery.setId(null); // Ensure new insert
            Delivery saved = deliveryRepository.save(delivery);
            return modelMapper.map(saved, CreateDeliveryDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error while adding delivery: " + e.getMessage(), e);
        }
    }

    // READ by ID
    public RetrieveDeliveryDTO getDeliveryById(Long id) {
        try {
            Delivery delivery = deliveryRepository.findById(id)
                    .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found with ID: " + id));
            return modelMapper.map(delivery, RetrieveDeliveryDTO.class);
        } catch (DeliveryNotFoundException e) {
            throw e; // rethrow custom exception
        } catch (Exception e) {
            throw new RuntimeException("Error while retrieving delivery: " + e.getMessage(), e);
        }
    }

    // READ all
    public List<RetrieveDeliveryDTO> getAllDeliveries() {
        try {
            List<Delivery> deliveries = deliveryRepository.findAll();
            return modelMapper.map(deliveries, new TypeToken<List<RetrieveDeliveryDTO>>() {}.getType());
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching all deliveries: " + e.getMessage(), e);
        }
    }

    // UPDATE Delivery
    public RetrieveDeliveryDTO updateDelivery(Long id, RetrieveDeliveryDTO dto) {
        try {
            Delivery existing = deliveryRepository.findById(id)
                    .orElseThrow(() -> new DeliveryNotFoundException("Cannot update. Delivery not found with ID: " + id));

            existing.setOrderId(dto.getOrderId());
            existing.setAddress(dto.getAddress());
            existing.setStatus(dto.getStatus()); // enum string from DTO
            existing.setDeliveryDate(dto.getDeliveryDate());

            Delivery saved = deliveryRepository.save(existing);
            return modelMapper.map(saved, RetrieveDeliveryDTO.class);
        } catch (DeliveryNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error while updating delivery: " + e.getMessage(), e);
        }
    }

    // DELETE Delivery
    public DeleteDeliveryDTO deleteDelivery(Long id) {
        try {
            Delivery existing = deliveryRepository.findById(id)
                    .orElseThrow(() -> new DeliveryNotFoundException("Cannot delete. Delivery not found with ID: " + id));
            deliveryRepository.delete(existing);
            return modelMapper.map(existing, DeleteDeliveryDTO.class);
        } catch (DeliveryNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting delivery: " + e.getMessage(), e);
        }
    }

    // DELIVERY Stats
    public DeliveryStatsDTO calculateDeliveryStats() {
        try {
            List<RetrieveDeliveryDTO> deliveries = getAllDeliveries();
            int totalDeliveries = deliveries.size();
            int pendingDeliveries = (int) deliveries.stream()
                    .filter(d -> DeliveryStatus.PENDING.equals(d.getStatus()))
                    .count();
            return new DeliveryStatsDTO(totalDeliveries, pendingDeliveries);
        } catch (Exception e) {
            throw new RuntimeException("Error while calculating delivery stats: " + e.getMessage(), e);
        }
    }

    // GET all statuses
    public List<String> getAllStatuses() {
        try {
            return List.of(
                    DeliveryStatus.PENDING.name(),
                    DeliveryStatus.IN_TRANSIT.name(),
                    DeliveryStatus.DELIVERED.name()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching delivery statuses: " + e.getMessage(), e);
        }
    }

    // GET all couriers
    public List<String> getAllCourierServices() {
        try {
            return List.of("KOOBIYO", "DOMEX");
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching courier services: " + e.getMessage(), e);
        }
    }
}
