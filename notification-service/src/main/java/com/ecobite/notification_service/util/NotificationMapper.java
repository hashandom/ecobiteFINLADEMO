package com.ecobite.notification_service.util;

import com.ecobite.notification_service.dto.response.NotificationResponse;
import com.ecobite.notification_service.entity.Notification;

public class NotificationMapper {
    public static NotificationResponse toResponse(Notification n) {

        return NotificationResponse.builder()
                .id(n.getId())
                .message(n.getMessage())
                .type(n.getType())
                .status(n.getStatus())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
