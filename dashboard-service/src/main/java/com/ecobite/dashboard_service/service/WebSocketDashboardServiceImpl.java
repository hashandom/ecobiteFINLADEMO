package com.ecobite.dashboard_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WebSocketDashboardServiceImpl implements WebSocketDashboardService{
    @Override
    public void sendLiveUpdate(
            String message
    ) {

        System.out.println(
                "Live Update: " + message
        );
    }
}
