package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.service.ContactService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.trkpo.ptinder.config.Constants.CONTACT_PATH;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class ContactControllerTest {

    ContactService contactService;
    ContactController contactController;

    private MockMvc mockMvc;

    @Before()
    public void init() {
        contactService = Mockito.mock(ContactService.class);
        contactController = new ContactController(contactService);
        mockMvc = standaloneSetup(contactController).build();
    }

    @Test
    public void testCanSendRequestToAskForContacts() throws Exception {
        mockMvc.perform(post("/" + CONTACT_PATH + "/request/{sourceGoogleId}/{destinationGoogleId}", "123", "321"))
                .andExpect(status().isOk())
                .andReturn();
        verify(contactService).requestUserContact("123", "321");
    }

    @Test
    public void testCanResponseWithContacts() throws Exception {
        mockMvc.perform(post("/" + CONTACT_PATH + "/response/{sourceGoogleId}/{destinationGoogleId}/{notificationId}", "123", "321", "1"))
                .andExpect(status().isOk())
                .andReturn();
        verify(contactService).responseUserContact("123", "321", "1");
    }
}