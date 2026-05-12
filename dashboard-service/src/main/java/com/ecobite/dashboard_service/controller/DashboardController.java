package com.ecobite.dashboard_service.controller;

import com.ecobite.dashboard_service.dto.response.ApiResponse;
import com.ecobite.dashboard_service.dto.response.DashboardOverviewResponse;
import com.ecobite.dashboard_service.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overview")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<DashboardOverviewResponse> getDashboardOverview() {

        return ApiResponse.success(
                "Dashboard overview fetched successfully",
                dashboardService.getDashboardOverview()
        );
    }

}
