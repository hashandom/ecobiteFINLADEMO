package com.ecobite.dashboard_service.kafka;
import com.ecobite.dashboard_service.dto.event.DashboardEvent;
import com.ecobite.dashboard_service.websocket.DashboardSocketPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardEventConsumer {
    private final DashboardSocketPublisher publisher;

    @KafkaListener(
            topics = "dashboard-events",
            groupId = "dashboard-group"
    )
    public void consume(DashboardEvent event) {

        System.out.println(
                "Dashboard Event Received: "
                        + event.getMessage()
        );

        publisher.publishUpdate(
                event.getMessage()
        );
    }
}
