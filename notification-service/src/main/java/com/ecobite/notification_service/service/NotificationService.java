package com.ecobite.notification_service.service;


import com.ecobite.notification_service.entity.Notification;

import java.util.List;

public interface NotificationService  {
    Notification saveNotification(Notification notification);

    List<Notification> getAllNotifications();

}
