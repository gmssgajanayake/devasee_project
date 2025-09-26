package com.devasee.product.services;

import com.devasee.product.entity.StationeryBrand;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.repo.StationeryBrandRepo;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationeryBrandService {

    private final StationeryBrandRepo stationeryBrandRepo;

    // Get all stationery brands
    public List<StationeryBrand> getAllBrands() {
        try {
            return stationeryBrandRepo.findAll();
        } catch (Exception exception) {
            throw new ProductNotFoundException("Stationery brands not found");
        }
    }

    // Create a new stationery brand
    public StationeryBrand createBrand(String brandName) {
        StationeryBrand stationeryBrand = new StationeryBrand();
        stationeryBrand.setName(brandName);

        try {
            return stationeryBrandRepo.save(stationeryBrand);
        } catch (Exception ex) {
            throw new InternalServerErrorException("Something went wrong when saving stationery brand");
        }
    }
}
