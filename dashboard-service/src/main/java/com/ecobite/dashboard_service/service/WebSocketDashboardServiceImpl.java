package com.ecobite.dashboard_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WebSocketDashboardServiceImpl implements WebSocketDashboardService{
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendLiveUpdate(String message) {

        messagingTemplate.convertAndSend("/topic/dashboard",
                message);
    }
}
