package com.devasee.product.controller;

import com.devasee.product.dto.StationeryDTO;
import com.devasee.product.services.StationeryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/product/stationery")
public class StationeryController {

    @Autowired
    private StationeryServices stationeryServices;

    @GetMapping("/allStationery")
    public List<StationeryDTO> getAllStationery() {
        return stationeryServices.getAllStationery();
    }
    @GetMapping("/{stationeryId}")
    public StationeryDTO getStationeryById(@PathVariable int stationeryId) {
        return stationeryServices.getStationeryById(stationeryId);
    }

    @PostMapping("/addStationery")
    public StationeryDTO saveStationery(@RequestBody StationeryDTO stationeryDTO) {
        return stationeryServices.saveStationery(stationeryDTO);
    }

    @PutMapping("/updateStationery")
    public StationeryDTO updateStationery(@RequestBody StationeryDTO stationeryDTO) {
        return stationeryServices.updateStationery(stationeryDTO);
    }

    @DeleteMapping("/deleteStationery")
    public boolean deleteStationery(@RequestBody StationeryDTO stationeryDTO) {
        return stationeryServices.deleteStationery(stationeryDTO);
    }
}
