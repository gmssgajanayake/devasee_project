package com.devasee.analytics.controller;

import com.devasee.analytics.dto.FullAnalyticsReportDTO;
import com.devasee.analytics.response.CustomResponse;
import com.devasee.analytics.services.AnalyticsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/analytics/admin")
@CrossOrigin
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsServices) {
        this.analyticsService = analyticsServices;
    }

    @GetMapping("/report")
    public CustomResponse<FullAnalyticsReportDTO> getReport() {
        FullAnalyticsReportDTO report = analyticsService.generateReport();
        return new CustomResponse<>(true, "Analytics report generated", report);
    }
}
