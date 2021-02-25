package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.entity.AnimalType;
import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.templates.PetAndGoogleId;
import com.trkpo.ptinder.service.ContactService;
import com.trkpo.ptinder.service.PetService;
import org.junit.Before;
import org.junit.Ignore;
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
import java.util.Optional;

import static com.trkpo.ptinder.config.Constants.PETS_PATH;
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
        when(petService.savePetForUser(any())).thenReturn(testPet);
        when(petService.updatePetInfo(any(), any())).thenReturn(testPet);
        when(petService.addNewAnimalType(type)).thenReturn(type);

        ArrayList<AnimalType> types = new ArrayList<>();
        types.add(type);
        types.add(new AnimalType());
        when(petService.getAllAnimalTypes()).thenReturn(types);

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
    public void testCanGetAllPetsForCurrentUser() throws Exception {
        MvcResult r = mockMvc.perform(get("/" + PETS_PATH + "/owner/{googleid}", TEST_GOOGLE_ID))
                .andExpect(status().isOk())
                .andReturn();
        String content = r.getResponse().getContentAsString();
        assertTrue(content.contains(TEST_PET_ID.toString()));
        assertEquals(3, content.split("},\\{").length);
    }

    @Test
    public void testCanAddNewPet() throws Exception {
        PetAndGoogleId petAndGoogleId = new PetAndGoogleId();
        petAndGoogleId.setGoogleId(TEST_GOOGLE_ID);
        petAndGoogleId.setPet(testPet);
        String requestJson = getObjectAsJson(petAndGoogleId);
        MvcResult result = mockMvc.perform(post("/" + PETS_PATH)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println(content);
        assertTrue(content.contains(TEST_PET_ID.toString()));
    }

    @Test
    public void testCanUpdatePet() throws Exception {
        testPet.setName("newName");
        PetAndGoogleId petAndGoogleId = new PetAndGoogleId();
        petAndGoogleId.setGoogleId(TEST_GOOGLE_ID);
        petAndGoogleId.setPet(testPet);
        String requestJson = getObjectAsJson(petAndGoogleId);
        MvcResult mvcResult = mockMvc.perform(put("/" + PETS_PATH + "/{petId}", TEST_PET_ID)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(testPet.getName()));
    }

    @Test
    public void testCanGetAllAnimalTypes() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/" + PETS_PATH + "/types"))
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(type.getType()));
        assertEquals(2, content.split("},\\{").length);
    }

    @Test
    public void testCanAddNewAnimalType() throws Exception {
        String requestJson = getObjectAsJson(type);
        mockMvc.perform(post("/" + PETS_PATH + "/types")
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.type").value(type.getType()))
                .andExpect(status().isOk());
    }


}