package com.ecobite.dashboard_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/test")
    public String sendMessage() {

        System.out.println("SENDING TEST MESSAGE");

        messagingTemplate.convertAndSend(
                "/topic/low-stock",
                "LOW STOCK ALERT TEST"
        );

        return "MESSAGE SENT";
    }
}
