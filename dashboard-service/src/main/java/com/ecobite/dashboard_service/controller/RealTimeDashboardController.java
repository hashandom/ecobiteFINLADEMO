package com.ecobite.dashboard_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/realtime")
@RequiredArgsConstructor
public class RealTimeDashboardController {
    @PostMapping("/publish")
    public String publish(
            @RequestBody String message
    ) {

        System.out.println(
                "Realtime Message: " + message
        );

        return "Published Successfully";
    }
}
