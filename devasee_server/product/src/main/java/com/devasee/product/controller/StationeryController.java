package com.devasee.product.controller;

import com.devasee.product.dto.*;
import com.devasee.product.response.CustomResponse;
import com.devasee.product.services.StationeryServices;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/product/stationery")
public class StationeryController {

    private final StationeryServices service;

    public StationeryController(StationeryServices service){
        this.service = service;
    }
        //-------------------------------------public-----------------------------------------------
    /**
     * Retrieve all stationery with pagination
     */
    @GetMapping("/public/all")
    public CustomResponse<Page<RetrieveStationeryDTO>> getAllStationery(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        return new CustomResponse<>(true,"Stationery list retrieved successfully",
                service.getAllStationery(page,size));
    }

    /**
     *  Retrieve stationery by ID
     */
    @GetMapping("/public/{id}")
    public CustomResponse<RetrieveStationeryDTO> getStationeryById(@PathVariable String id){
        return new CustomResponse<>(true,"Stationery found",
                service.getStationeryById(id));
    }

    /**
     * Search stationery by name
     */
    @GetMapping("/public/search")
    public CustomResponse<Page<RetrieveStationeryDTO>> searchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return new CustomResponse<>(true,"Stationery search results",
                service.searchStationeryByName(name,page,size));
    }

    //-------------------------------------Admin-----------------------------------------------
    /**
     *  Create new stationery (with optional image upload)
     */
    @PostMapping("/admin/addStationery")
    public CustomResponse<CreateStationeryDTO> addStationery(
            @RequestParam("stationery") String stationeryJson,
            @RequestParam(value = "file", required = false) MultipartFile file
    ){
        return new CustomResponse<>(true,"Stationery created successfully",
                service.saveStationery(stationeryJson,file));
    }

    /**
     *  Update existing stationery
     */
    @PutMapping("/admin/updateStationery")
    public CustomResponse<RetrieveStationeryDTO> updateStationery(
            @RequestParam("stationery") String stationeryJson,
            @RequestParam(value = "file", required = false) MultipartFile file
    ){
        return new CustomResponse<>(true,"Stationery updated successfully",
                service.updateStationery(stationeryJson,file));
    }

    /**
     * Delete stationery by ID
     */
    @DeleteMapping("/admin/deleteID/{id}")
    public CustomResponse<DeleteStationeryDTO> deleteStationery(@PathVariable String id){
        return new CustomResponse<>(true,"Stationery deleted successfully",
                service.deleteStationery(id));
    }
}
