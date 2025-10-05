package com.devasee.product.services.printing;

import com.devasee.product.entity.printing.PrintProductType;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.repo.printing.PrintProductTypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrintProductTypeService {

    private final PrintProductTypeRepo typeRepo;

    public PrintProductType createType(PrintProductType type) {
        return typeRepo.save(type);
    }

    public List<PrintProductType> getAllTypes() {
        return typeRepo.findAll();
    }

    public PrintProductType getTypeByName(String name) {
        return typeRepo.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException("Type not found: " + name));
    }

}
