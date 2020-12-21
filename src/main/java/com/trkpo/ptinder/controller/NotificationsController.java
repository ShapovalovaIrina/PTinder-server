package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.entity.Notifications;
import com.trkpo.ptinder.service.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{googleid}")
    public List<Notifications> getPet(@PathVariable("googleid") String googleId) {
        return notificationsService.getNotificationsForUser(googleId);
    }
}
