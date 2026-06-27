package com.ecobite.dashboard_service.controller;

import com.ecobite.dashboard_service.websocket.DashboardSocketPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/realtime")
@RequiredArgsConstructor
public class RealTimeDashboardController {
    private final DashboardSocketPublisher publisher;


    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    @PostMapping("/publish")
    public String publish(
            @RequestBody Map<String, String> body) {

        publisher.publishUpdate(
                body.get("message")
        );

        return "Published Successfully";
    }
}
