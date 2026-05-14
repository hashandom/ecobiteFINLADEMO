package com.ecobite.dashboard_service.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class DashboardWebSocketHandler extends TextWebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {

        System.out.println("Connected");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message)
            throws Exception {

        session.sendMessage(new TextMessage("Dashboard Update"));
    }
}
