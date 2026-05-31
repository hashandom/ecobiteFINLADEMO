package com.ecobite.dashboard_service.client;

import com.ecobite.dashboard_service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "notification-service", configuration = FeignConfig.class)
public interface NotificationClient {
    @GetMapping("/notifications/unread/count")
    Long getUnreadCount();

    @GetMapping("/notifications/count")
    Long getTotalNotifications();
}
