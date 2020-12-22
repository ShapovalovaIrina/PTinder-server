package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.trkpo.ptinder.config.Constants.CONTACT_PATH;

@RestController
@RequestMapping(CONTACT_PATH)
public class ContactController {
    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("request/{sourceGoogleId}/{destinationGoogleId}")
    public void sendContactRequest(
            @PathVariable("sourceGoogleId") String sourceGoogleId,
            @PathVariable("destinationGoogleId") String destinationGoogleId) {
        contactService.requestUserContact(sourceGoogleId, destinationGoogleId);
    }

    @PostMapping("response/{sourceGoogleId}/{destinationGoogleId}/{notificationId}")
    public void sendContactAnswer(
            @PathVariable("sourceGoogleId") String sourceGoogleId,
            @PathVariable("destinationGoogleId") String destinationGoogleId,
            @PathVariable("notificationId") String notificationId) {
        contactService.responseUserContact(sourceGoogleId, destinationGoogleId, notificationId);
    }
}
