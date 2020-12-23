package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.entity.Notifications;
import com.trkpo.ptinder.service.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.trkpo.ptinder.config.Constants.NOTIFICATIONS_PATH;

@RestController
@RequestMapping(NOTIFICATIONS_PATH)
public class NotificationsController {
    private final NotificationsService notificationsService;

    @Autowired
    public NotificationsController(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    @GetMapping("/{googleId}")
    public List<Notifications> getNotificationsForUser(@PathVariable("googleId") String googleId) {
        return notificationsService.getNotificationsForUser(googleId);
    }

    @PostMapping("/{notificationId}")
    public void markAsRead(@PathVariable("notificationId") String notificationId) {
        notificationsService.markAsRead(notificationId);
    }
}
