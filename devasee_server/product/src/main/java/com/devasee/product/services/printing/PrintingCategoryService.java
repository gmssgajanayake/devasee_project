package com.devasee.product.services.printing;

import com.devasee.product.entity.printing.PrintingCategory;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.repo.printing.PrintingCategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrintingCategoryService {

    private final PrintingCategoryRepo categoryRepo;

    public PrintingCategory createCategory(PrintingCategory category) {
        return categoryRepo.save(category);
    }

    public List<PrintingCategory> getAllCategories() {
        return categoryRepo.findAll();
    }

    public PrintingCategory getCategoryByName(String name) {
        return categoryRepo.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException("Category not found: " + name));
    }

}
