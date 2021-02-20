package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.service.PetService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Optional;

import static com.trkpo.ptinder.config.Constants.PETS_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class PetControllerTest extends AbstractControllerTest {

    @Mock
    PetService petService;

    @InjectMocks
    PetController petController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        initPetAndUser();
        when(petService.findPet(TEST_PET_ID)).thenReturn(java.util.Optional.ofNullable(testPet));
        when(petService.findPet(0L)).thenReturn(Optional.of(new Pet()));
        ArrayList<Pet> petsForUser = new ArrayList<>();
        petsForUser.add(testPet);
        petsForUser.add(new Pet());
        petsForUser.add(new Pet());
        when(petService.findPetsForUser(TEST_GOOGLE_ID)).thenReturn(petsForUser);

        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(petController).build();
    }

    @Test
    public void testCanGetExistingPetById() throws Exception {
        mockMvc.perform(get("/" + PETS_PATH + "/{id}", TEST_PET_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.name").value(testPet.getName()))
                .andReturn();
    }

    @Test
    public void testThereAreNoExistingPets() throws Exception {
        mockMvc.perform(get("/" + PETS_PATH + "/{id}", 0L))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.name").doesNotExist())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testCanFetAllPetsForCurrentUser() throws Exception {
        MvcResult r = mockMvc.perform(get("/" + PETS_PATH + "/owner/{googleid}", TEST_GOOGLE_ID))
                .andExpect(status().isOk())
                .andReturn();
        String content = r.getResponse().getContentAsString();
        assertTrue(content.contains(TEST_PET_ID.toString()));
        assertEquals(3, content.split("},\\{").length);
    }


}