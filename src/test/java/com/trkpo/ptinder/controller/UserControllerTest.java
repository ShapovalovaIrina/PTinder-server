package com.trkpo.ptinder.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static com.trkpo.ptinder.config.Constants.USERS_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest extends AbstractControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        initPetAndUser();
        ArrayList<User> listOfUsers = new ArrayList<>();
        listOfUsers.add(testUser);
        listOfUsers.add(new User());
        listOfUsers.add(new User());
        when(userService.getAllUsers()).thenReturn(listOfUsers);
        when(userService.findUser(TEST_GOOGLE_ID)).thenReturn(testUser);
        when(userService.isCurrentUserExist(TEST_GOOGLE_ID)).thenReturn(true);
        when(userService.isCurrentUserExist("000")).thenReturn(false);
        when(userService.addUser(any())).thenReturn(testUser);
        when(userService.updateUser(any(), any())).thenReturn(testUser);
        when(userService.getUserInfoStatus(TEST_GOOGLE_ID)).thenReturn(true);
        when(userService.getUserInfoStatus("000")).thenReturn(false);
        testUser.setContactInfoPublic(true);
        when(userService.setUserInfoStatus(TEST_GOOGLE_ID)).thenReturn(testUser);

        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(userController).build();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        MvcResult result = mockMvc.perform(get("/" + USERS_PATH))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(TEST_GOOGLE_ID));
        assertEquals(3, content.split("},\\{").length);
    }

    @Test
    public void testGetCurrentUser() throws Exception {
        mockMvc.perform(get("/" + USERS_PATH + "/{googleId}", TEST_GOOGLE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.googleId").value(TEST_GOOGLE_ID))
                .andReturn();
    }

    @Test
    public void testIfUserWithSuchGoogleIdExists() throws Exception {
        MvcResult firstResult = mockMvc.perform(get("/" + USERS_PATH + "/exists/" + "{googleId}", TEST_GOOGLE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();
        String firstContent = firstResult.getResponse().getContentAsString();
        assertEquals("true", firstContent);

        MvcResult secondResult = mockMvc.perform(get("/" + USERS_PATH + "/exists/" + "{googleId}", "000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();
        String secondContent = secondResult.getResponse().getContentAsString();
        assertEquals("false", secondContent);
    }

    @Test
    public void testThatCanAddUser() throws Exception {
        String requestJson = getObjectAsJson(testUser);

        MvcResult mvcResult = mockMvc.perform(post("/" + USERS_PATH).contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(testUser.getFirstName()));
    }

    @Test
    public void testCanUpdateUser() throws Exception {
        testUser.setFirstName("newFirstName");
        String requestJson = getObjectAsJson(testUser);
        MvcResult mvcResult = mockMvc.perform(put("/" + USERS_PATH + "/{googleId}", TEST_GOOGLE_ID)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(testUser.getFirstName()));
    }

    @Test
    public void testCanDeleteUser() throws Exception {
        UserService userService = Mockito.mock(UserService.class);
        UserController userController = new UserController(userService);
        MockMvc mockMvc = standaloneSetup(userController).build();
        mockMvc.perform(delete("/" + USERS_PATH + "/{googleId}", TEST_GOOGLE_ID))
                .andExpect(status().isOk())
                .andReturn();
        verify(userService).deleteUser(TEST_GOOGLE_ID);
    }

    @Test
    public void testCanGetUserInfoStatus() throws Exception {
        MvcResult firstResult = mockMvc.perform(get("/" + USERS_PATH + "/info/{googleId}", TEST_GOOGLE_ID))
                .andExpect(status().isOk())
                .andReturn();
        String firstContent = firstResult.getResponse().getContentAsString();
        assertEquals("true", firstContent);

        MvcResult secondResult = mockMvc.perform(get("/" + USERS_PATH + "/info/{googleId}", "000"))
                .andExpect(status().isOk())
                .andReturn();
        String secondContent = secondResult.getResponse().getContentAsString();
        assertEquals("false", secondContent);
    }

    @Test
    public void testCanSetNewUserInfoStatus() throws Exception {
        mockMvc.perform(post("/" + USERS_PATH + "/info/{googleId}", TEST_GOOGLE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.contactInfoPublic").value("true"))
                .andReturn();
    }

}
