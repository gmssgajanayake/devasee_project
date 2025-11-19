package com.devasee.product.services.stationery;

import com.devasee.product.entity.stationery.StationeryCategory;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.repo.stationery.StationeryCategoryRepo;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationeryCategoryService {

    private final StationeryCategoryRepo stationeryCategoryRepo;

    // Get all stationery categories
    public List<StationeryCategory> getAllCategories() {
        try {
            return stationeryCategoryRepo.findAll();
        } catch (Exception exception) {
            throw new ProductNotFoundException("Stationery categories not found");
        }
    }

    // Create a new stationery category
    public StationeryCategory createCategory(String categoryName) {
        StationeryCategory stationeryCategory = new StationeryCategory();
        stationeryCategory.setName(categoryName);

        try {
            return stationeryCategoryRepo.save(stationeryCategory);
        } catch (Exception ex) {
            throw new InternalServerErrorException("Something went wrong when saving stationery category");
        }
    }
}
