package com.ecobite.dashboard_service.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DashboardSocketPublisher {
    private final SimpMessagingTemplate messagingTemplate;

    public void publishUpdate(String message) {
        messagingTemplate.convertAndSend(
                "/topic/dashboard",
                message
        );
    }



}
