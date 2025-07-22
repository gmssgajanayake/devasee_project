package com.devasee.analytics.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/analytics")
public class AnalyticsController {

    @GetMapping("/all")
    public String getAllAnalytics(){
        return "getting all analytics";
    }
}
