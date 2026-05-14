package com.ecobite.dashboard_service.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class DashboardSocketPublisher {
    public void publishDashboardUpdate(
            Object payload
    ) {

        System.out.println(
                "Dashboard Update: " + payload
        );
    }

    public void publishInventoryUpdate(
            Object payload
    ) {

        System.out.println(
                "Inventory Update: " + payload
        );
    }

    public void publishReorderUpdate(
            Object payload
    ) {

        System.out.println(
                "Reorder Update: " + payload
        );
    }



}
