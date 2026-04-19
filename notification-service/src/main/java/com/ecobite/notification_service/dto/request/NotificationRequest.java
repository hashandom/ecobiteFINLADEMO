package com.ecobite.notification_service.dto.request;

import lombok.Data;

@Data
public class NotificationRequest {
    private String message;
    private String type;
}
