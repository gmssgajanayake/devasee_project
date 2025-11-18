package com.devasee.product.controller;

import com.devasee.product.dto.stationery.CreateStationeryDTO;
import com.devasee.product.dto.stationery.DeleteStationeryDTO;
import com.devasee.product.dto.stationery.RetrieveStationeryDTO;
import com.devasee.product.entity.stationery.StationeryCategory;
import com.devasee.product.entity.stationery.StationeryBrand;
import com.devasee.product.response.CustomResponse;
import com.devasee.product.services.stationery.StationeryCategoryService;
import com.devasee.product.services.stationery.StationeryBrandService;
import com.devasee.product.services.stationery.StationeryServices;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/product/stationery")
public class StationeryController {

    private final StationeryServices stationeryServices;
    private final StationeryCategoryService categoryService;
    private final StationeryBrandService brandService;

    public StationeryController(
            StationeryServices stationeryServices,
            StationeryCategoryService categoryService,
            StationeryBrandService brandService
    ) {
        this.stationeryServices = stationeryServices;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    // --------------------------------- Public ---------------------------------

    @GetMapping
    public CustomResponse<Page<RetrieveStationeryDTO>> getAllStationery(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<RetrieveStationeryDTO> stationeryPage = stationeryServices.getAllStationery(page, size);
        return new CustomResponse<>(true, "Stationery list retrieved successfully", stationeryPage);
    }

    @GetMapping("/{stationeryId}")
    public CustomResponse<RetrieveStationeryDTO> getStationeryById(@PathVariable String stationeryId) {
        RetrieveStationeryDTO dto = stationeryServices.getStationeryById(stationeryId);
        return new CustomResponse<>(true, "Stationery found", dto);
    }

    @GetMapping("/search")
    public CustomResponse<Page<RetrieveStationeryDTO>> searchStationeryByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<RetrieveStationeryDTO> results = stationeryServices.searchStationeryByTitle(title, page, size);
        return new CustomResponse<>(true, "Stationery search results", results);
    }

    @GetMapping("/filter")
    public CustomResponse<Page<RetrieveStationeryDTO>> filterStationeryByTerm(
            @RequestParam String term,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<RetrieveStationeryDTO> filtered = stationeryServices.filterStationeryByTerm(term, page, size);
        return new CustomResponse<>(true, "Filtered stationery by term: " + term, filtered);
    }

    @GetMapping("/categories")
    public CustomResponse<List<StationeryCategory>> getAllCategories() {
        List<StationeryCategory> categories = categoryService.getAllCategories();
        return new CustomResponse<>(true, "All stationery categories fetched", categories);
    }

    @GetMapping("/brands")
    public CustomResponse<List<StationeryBrand>> getAllBrands() {
        List<StationeryBrand> brands = brandService.getAllBrands();
        return new CustomResponse<>(true, "All stationery brands fetched", brands);
    }

    // --------------------------------- Admin ---------------------------------

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CustomResponse<CreateStationeryDTO> saveStationery(
            @RequestParam("stationery") String stationeryJson,
            @RequestParam(value = "mainFile", required = false) MultipartFile mainFile,
            @RequestParam(value = "otherFiles", required = false) List<MultipartFile> otherFiles
    ) {
        CreateStationeryDTO dtoResponse = stationeryServices.saveStationery(stationeryJson, mainFile, otherFiles);
        return new CustomResponse<>(true, "Stationery saved successfully", dtoResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public CustomResponse<RetrieveStationeryDTO> updateStationery(
            @RequestParam("stationery") String stationeryJson,
            @RequestParam(value = "mainFile", required = false) MultipartFile mainFile,
            @RequestParam(value = "otherFiles", required = false) List<MultipartFile> otherFiles
    ) {
        RetrieveStationeryDTO dto = stationeryServices.updateStationery(stationeryJson, mainFile, otherFiles);
        return new CustomResponse<>(true, "Stationery updated successfully", dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{stationeryId}")
    public CustomResponse<DeleteStationeryDTO> deleteStationery(@PathVariable String stationeryId) {
        DeleteStationeryDTO dto = stationeryServices.deleteStationery(stationeryId);
        return new CustomResponse<>(true, "Stationery deleted successfully", dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/az")
    public CustomResponse<RetrieveStationeryDTO> deleteOtherImgFile(
            @RequestParam("fileName") String fileName,
            @RequestParam("productId") String productId
    ) {
        RetrieveStationeryDTO updated = stationeryServices.deleteImgFromAzureBlobByFileName(fileName, productId);
        return new CustomResponse<>(true, "Image deleted successfully", updated);
    }

    // ----------------- NEW ENDPOINTS -----------------

    // POST /api/v1/product/stationery/brands
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/brands")
    public CustomResponse<StationeryBrand> createBrand(@RequestParam String name) {
        StationeryBrand created = brandService.createBrand(name);
        return new CustomResponse<>(true, "Stationery brand created successfully", created);
    }

    // POST /api/v1/product/stationery/categories
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/categories")
    public CustomResponse<StationeryCategory> createCategory(@RequestParam String name) {
        StationeryCategory created = categoryService.createCategory(name);
        return new CustomResponse<>(true, "Stationery category created successfully", created);
    }
}
