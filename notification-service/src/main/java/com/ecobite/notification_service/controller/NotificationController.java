package com.ecobite.notification_service.controller;

import com.ecobite.notification_service.entity.Notification;
import com.ecobite.notification_service.repository.NotificationRepository;
import com.ecobite.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationRepository repository;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    @GetMapping
    public List<Notification> getAll() {
        return repository.findAll();
    }

//    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
//    @GetMapping("/unread/count")
//    public Long getUnreadCount() {
//        return repository.countByIsReadFalse();
//    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    @GetMapping("/count")
    public Long getCount() {
        return repository.count();
    }
}
