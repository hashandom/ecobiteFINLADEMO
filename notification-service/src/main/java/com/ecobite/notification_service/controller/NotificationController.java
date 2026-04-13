package com.ecobite.notification_service.controller;

import com.ecobite.notification_service.entity.Notification;
import com.ecobite.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<Notification> getNotifications() {
        return notificationService.getAllNotifications();
    }
}
