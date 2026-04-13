package com.ecobite.notification_service.service;

import com.ecobite.notification_service.entity.Notification;
import com.ecobite.notification_service.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository repository;

    @Override
    public Notification saveNotification(Notification notification) {

        notification.setCreatedAt(LocalDateTime.now());

        return repository.save(notification);
    }

    @Override
    public List<Notification> getAllNotifications() {
        return repository.findAll();
    }

}
