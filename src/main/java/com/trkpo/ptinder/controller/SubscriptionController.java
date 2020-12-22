package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.entity.templates.GoogleId;
import com.trkpo.ptinder.service.UserService;
import org.springframework.web.bind.annotation.*;

import static com.trkpo.ptinder.config.Constants.SUBSCRIPTION_PATH;

@RestController
@RequestMapping(SUBSCRIPTION_PATH)
public class SubscriptionController {

    private final UserService userService;

    public SubscriptionController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{userId}")
    public void subscribeOnUser(@PathVariable("userId") String userId, @RequestBody GoogleId aimUser) {
        userService.subscribe(userId, aimUser);
    }

    @DeleteMapping("/{userId}/{targetId}")
    public void unsubscribeFromUser(@PathVariable("userId") String userId, @PathVariable("targetId") String aimUser) {
        userService.unsubscribe(userId, aimUser);
    }

    @GetMapping("/check/{userId}/{targetId}")
    public boolean checkSubscriptionOnUser(@PathVariable("userId") String userId, @PathVariable("targetId") String aimUser) {
        return userService.checkSubscription(userId, aimUser);
    }
}
