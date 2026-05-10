package com.ecobite.dashboard_service.controller;

import com.ecobite.dashboard_service.dto.Response;
import com.ecobite.dashboard_service.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public Response getSummary() {
        return dashboardService.getSummary();
    }
}
