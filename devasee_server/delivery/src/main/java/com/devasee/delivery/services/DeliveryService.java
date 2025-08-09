package com.devasee.delivery.services;

import com.devasee.delivery.dto.CreateDeliveryDTO;
import com.devasee.delivery.dto.DeleteDeliveryDTO;
import com.devasee.delivery.dto.RetrieveDeliveryDTO;
import com.devasee.delivery.entity.Delivery;
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

    // CREATE: ensure ID is null before saving and return saved entity mapped back
    public CreateDeliveryDTO addDelivery(CreateDeliveryDTO dto) {
        Delivery delivery = modelMapper.map(dto, Delivery.class);
        delivery.setId(null); // important: force new insert
        Delivery saved = deliveryRepository.save(delivery);
        return modelMapper.map(saved, CreateDeliveryDTO.class);
    }

    // READ by ID
    public RetrieveDeliveryDTO getDeliveryById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found with ID: " + id));
        return modelMapper.map(delivery, RetrieveDeliveryDTO.class);
    }

    // READ all
    public List<RetrieveDeliveryDTO> getAllDeliveries() {
        List<Delivery> deliveries = deliveryRepository.findAll();
        return modelMapper.map(deliveries, new TypeToken<List<RetrieveDeliveryDTO>>() {}.getType());
    }

    // UPDATE: find existing, update fields, save, return updated mapped dto
    public RetrieveDeliveryDTO updateDelivery(Long id, RetrieveDeliveryDTO dto) {
        Delivery existing = deliveryRepository.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException("Cannot update. Delivery not found with ID: " + id));

        // Update fields explicitly to avoid accidental ID overwrite
        existing.setOrderId(dto.getOrderId());
        existing.setAddress(dto.getAddress());
        existing.setStatus(dto.getStatus());
        existing.setDeliveryDate(dto.getDeliveryDate());

        Delivery saved = deliveryRepository.save(existing);
        return modelMapper.map(saved, RetrieveDeliveryDTO.class);
    }

    // DELETE: find existing, delete, return deleted dto
    public DeleteDeliveryDTO deleteDelivery(Long id) {
        Delivery existing = deliveryRepository.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException("Cannot delete. Delivery not found with ID: " + id));
        deliveryRepository.delete(existing);
        return modelMapper.map(existing, DeleteDeliveryDTO.class);
    }
}
