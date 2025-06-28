package com.devasee.product.services;

import com.devasee.product.dto.StationeryDTO;
import com.devasee.product.entity.Stationery;
import com.devasee.product.repo.StationeryRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class StationeryServices {

    @Autowired
    private StationeryRepo stationeryRepo;

    @Autowired
    private ModelMapper modelMapper;

    public List<StationeryDTO> getAllStationery() {
        return modelMapper.map(stationeryRepo.findAll(), new TypeToken<List<StationeryDTO>>() {}.getType());
    }

    public StationeryDTO getStationeryById(int stationeryId) {
        return modelMapper.map(stationeryRepo.findById(stationeryId), StationeryDTO.class);
    }

    public StationeryDTO saveStationery(StationeryDTO stationeryDTO) {
        stationeryRepo.save(modelMapper.map(stationeryDTO, Stationery.class));
        return stationeryDTO;
    }

    public StationeryDTO updateStationery(StationeryDTO stationeryDTO) {
        stationeryRepo.save(modelMapper.map(stationeryDTO, Stationery.class));
        return stationeryDTO;
    }

    public boolean deleteStationery(StationeryDTO stationeryDTO) {
        stationeryRepo.delete(modelMapper.map(stationeryDTO, Stationery.class));
        return true;
    }
}
