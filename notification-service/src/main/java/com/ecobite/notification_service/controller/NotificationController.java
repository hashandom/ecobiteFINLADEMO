package com.ecobite.notification_service.controller;

import com.ecobite.notification_service.entity.Notification;
import com.ecobite.notification_service.repository.NotificationRepository;
import com.ecobite.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationRepository repository;
    private final NotificationService notificationService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<Notification> getAll() {
        return repository.findAll();
    }

//    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
//    @GetMapping("/unread/count")
//    public Long getUnreadCount() {
//        return repository.countByIsReadFalse();
//    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/count")
    public Long getCount() {
        return repository.count();
    }


    // Role based notifications
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    @GetMapping("/role/{role}")
    public List<Notification> getByRole(
            @PathVariable String role) {

        return notificationService.getNotificationsByRole(role);
    }

    // Unread count by role
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    @GetMapping("/unread/count/{role}")
    public Long unreadCount(
            @PathVariable String role) {

        return notificationService.getUnreadCount(role);
    }

    // Total count by role
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    @GetMapping("/count/{role}")
    public Long totalCount(
            @PathVariable String role) {

        return notificationService.getTotalCount(role);
    }

    // Mark notification as read
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    @PutMapping("/{id}/read")
    public String markAsRead(
            @PathVariable Long id) {

        notificationService.markAsRead(id);

        return "Notification marked as read";
    }
}
