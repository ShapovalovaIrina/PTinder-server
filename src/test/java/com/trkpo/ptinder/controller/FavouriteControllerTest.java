package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.service.FavouriteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.trkpo.ptinder.config.Constants.FAVOURITE_PATH;
import static com.trkpo.ptinder.config.Constants.PETS_PATH;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class FavouriteControllerTest extends AbstractControllerTest {
    private MockMvc mockMvc;
    FavouriteService favouriteService;

    @Before
    public void init() {
        initPetAndUser();
        favouriteService = Mockito.mock(FavouriteService.class);
        FavouriteController favouriteController = new FavouriteController(favouriteService);
        Set<Pet> pets = new HashSet<>();
        pets.add(testPet);
        pets.add(new Pet());
        when(favouriteService.findFavouriteForUser(TEST_GOOGLE_ID)).thenReturn(pets);
        when(favouriteService.findFavouriteIdForUser(TEST_GOOGLE_ID)).thenReturn(Collections.singleton(1L));

        Set<User> users = new HashSet<>();
        users.add(testUser);
        users.add(new User());
        when(favouriteController.getUsersForPet(testPet)).thenReturn(users);
        mockMvc = standaloneSetup(favouriteController).build();
    }

    @Test
    public void testCanGetFavouritePetsForUser() throws Exception {
        mockMvc.perform(get("/" + FAVOURITE_PATH + "/user/full/{googleid}", TEST_GOOGLE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].name").doesNotExist())
                .andExpect(jsonPath("$[1].name").value(testPet.getName()))
                .andReturn();
    }

    @Test
    public void testGetFavouritePetsIdsForUser() throws Exception {
        mockMvc.perform(get("/" + FAVOURITE_PATH + "/user/id/{googleid}", TEST_GOOGLE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0]").value(1L))
                .andReturn();
    }

    @Test
    public void testCanAddNewFavouritePet() throws Exception {
        mockMvc.perform(post("/" + FAVOURITE_PATH + "/{petid}/user/{googleid}", TEST_PET_ID, TEST_GOOGLE_ID))
                .andExpect(status().isOk())
                .andReturn();
        verify(favouriteService).addToFavouriteForUser(TEST_PET_ID, TEST_GOOGLE_ID);
    }

    @Test
    public void testCanDeletePetFromFavourites() throws Exception {
        mockMvc.perform(delete("/" + FAVOURITE_PATH + "/{petid}/user/{googleid}", TEST_PET_ID, TEST_GOOGLE_ID))
                .andExpect(status().isOk())
                .andReturn();
        verify(favouriteService).deleteFromFavouriteById(TEST_PET_ID, TEST_GOOGLE_ID);
    }
}