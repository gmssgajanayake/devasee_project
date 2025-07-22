package com.devasee.delivery.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/delivery")
public class DeliveryController {

    @GetMapping("/all")
    public String getAllRegisteredDeliveries(){
        return "get all deliveries";
    }
}
