package com.ecobite.notification_service.repository;

import com.ecobite.notification_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Long countByIsReadFalse();
}
