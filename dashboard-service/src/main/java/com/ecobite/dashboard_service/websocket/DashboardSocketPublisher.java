package com.ecobite.dashboard_service.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DashboardSocketPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Publish live dashboard updates
     */
    public void publishDashboardUpdate(Object payload) {

        messagingTemplate.convertAndSend(
                "/topic/dashboard",
                payload
        );
    }

    /**
     * Publish inventory updates
     */
    public void publishInventoryUpdate(Object payload) {

        messagingTemplate.convertAndSend(
                "/topic/inventory",
                payload
        );
    }

    /**
     * Publish reorder alerts
     */
    public void publishReorderAlert(Object payload) {

        messagingTemplate.convertAndSend(
                "/topic/reorders",
                payload
        );
    }

    /**
     * Publish notification updates
     */
    public void publishNotificationUpdate(Object payload) {

        messagingTemplate.convertAndSend(
                "/topic/notifications",
                payload
        );
    }


}
