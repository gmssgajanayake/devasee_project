package com.devasee.product.services;

import com.devasee.product.entity.PrintingMaterial;
import com.devasee.product.exception.ProductNotFoundException;
import com.devasee.product.repo.PrintingMaterialRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrintingMaterialService {

    private final PrintingMaterialRepo materialRepo;

    public PrintingMaterial createMaterial(PrintingMaterial material) {
        return materialRepo.save(material);
    }

    public List<PrintingMaterial> getAllMaterials() {
        return materialRepo.findAll();
    }

    public PrintingMaterial getMaterialByName(String name) {
        return materialRepo.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException("Material not found: " + name));
    }


}
