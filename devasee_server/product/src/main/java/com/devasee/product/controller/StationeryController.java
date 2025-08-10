package com.devasee.product.controller;

import com.devasee.product.dto.CreateStationeryDTO;
import com.devasee.product.dto.DeleteStationeryDTO;
import com.devasee.product.dto.RetrieveStationeryDTO;
import com.devasee.product.response.CustomResponse;
import com.devasee.product.services.StationeryServices;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/product/stationery")
public class StationeryController {

    private final StationeryServices stationeryServices;

    public StationeryController(StationeryServices stationeryServices) {
        this.stationeryServices = stationeryServices;
    }

    @GetMapping("/public/allStationery")
    public CustomResponse<List<RetrieveStationeryDTO>> getAllStationery() {
        List<RetrieveStationeryDTO> list = stationeryServices.getAllStationery();
        return new CustomResponse<>(true, "Stationery items found", list);
    }

    @GetMapping("/public/{id}")
    public CustomResponse<RetrieveStationeryDTO> getStationeryById(@PathVariable int id) {
        RetrieveStationeryDTO dto = stationeryServices.getStationeryById(id);
        return new CustomResponse<>(true, "Stationery item found", dto);
    }

    @PostMapping("/addStationery")
    public CustomResponse<CreateStationeryDTO> saveStationery(@RequestBody CreateStationeryDTO dto) {
        CreateStationeryDTO saved = stationeryServices.saveStationery(dto);
        return new CustomResponse<>(true, "Stationery item saved successfully", saved);
    }

    @PutMapping("/updateStationery")
    public CustomResponse<RetrieveStationeryDTO> updateStationery(@RequestBody RetrieveStationeryDTO dto) {
        RetrieveStationeryDTO updated = stationeryServices.updateStationery(dto);
        return new CustomResponse<>(true, "Stationery item updated successfully", updated);
    }

    @DeleteMapping("/deleteStationery/{id}")
    public CustomResponse<DeleteStationeryDTO> deleteStationery(@PathVariable int id) {
        DeleteStationeryDTO deleted = stationeryServices.deleteStationery(id);
        return new CustomResponse<>(true, "Stationery item deleted successfully", deleted);
    }
}
