package com.devasee.delivery.services;

import com.devasee.delivery.dto.DeliveryDTO;
import com.devasee.delivery.entity.Delivery;
import com.devasee.delivery.repo.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    // Convert Entity to DTO
    private DeliveryDTO convertToDTO(Delivery delivery) {
        DeliveryDTO dto = new DeliveryDTO();
        dto.setId(delivery.getId());
        dto.setOrderId(delivery.getOrderId());
        dto.setAddress(delivery.getAddress());
        dto.setStatus(delivery.getStatus());
        dto.setDeliveryDate(delivery.getDeliveryDate());
        return dto;
    }

    // Convert DTO to Entity
    private Delivery convertToEntity(DeliveryDTO dto) {
        Delivery delivery = new Delivery();
        delivery.setId(dto.getId());
        delivery.setOrderId(dto.getOrderId());
        delivery.setAddress(dto.getAddress());
        delivery.setStatus(dto.getStatus());
        delivery.setDeliveryDate(dto.getDeliveryDate());
        return delivery;
    }

    // Add new delivery
    public DeliveryDTO addDelivery(DeliveryDTO dto) {
        Delivery delivery = deliveryRepository.save(convertToEntity(dto));
        return convertToDTO(delivery);
    }

    // Get delivery by ID
    public DeliveryDTO getDeliveryById(Long id) {
        return deliveryRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    // Get all deliveries
    public List<DeliveryDTO> getAllDeliveries() {
        return deliveryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Update delivery
    public DeliveryDTO updateDelivery(Long id, DeliveryDTO dto) {
        if (!deliveryRepository.existsById(id)) return null;
        dto.setId(id);
        Delivery updated = deliveryRepository.save(convertToEntity(dto));
        return convertToDTO(updated);
    }

    // Delete delivery
    public boolean deleteDelivery(Long id) {
        if (!deliveryRepository.existsById(id)) return false;
        deliveryRepository.deleteById(id);
        return true;
    }
}
