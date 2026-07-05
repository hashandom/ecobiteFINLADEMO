package com.ecobite.notification_service.repository;

import com.ecobite.notification_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification>
    findByTargetRoleOrderByCreatedAtDesc(String role);
    Long countByTargetRole(String role);
    Long countByTargetRoleAndIsReadFalse(String role);
    List<Notification>
    findByTargetRoleAndIsReadFalseOrderByCreatedAtDesc(
            String role
    );
}
