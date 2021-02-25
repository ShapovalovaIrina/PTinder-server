package com.trkpo.ptinder.controller;

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

import java.util.Arrays;
import java.util.Collections;

import static com.trkpo.ptinder.config.Constants.SEARCH_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class SearchControllerTest extends AbstractControllerTest {

    @Mock
    PetService petService;

    @InjectMocks
    SearchController searchController;

    private MockMvc mockMvc;

    @Before
    public void init() {
        initPetAndUser();
        when(petService.getAllAddresses()).thenReturn(Arrays.asList("Peter", "Moscow", "Kirovsk"));
        when(petService.findPetsWithFilters(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Collections.singletonList(testPet));

        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(searchController).build();
    }

    @Test
    public void testGetAllAddresses() throws Exception {
        MvcResult r = mockMvc.perform(get("/" + SEARCH_PATH + "/address"))
                .andExpect(status().isOk())
                .andReturn();
        String content = r.getResponse().getContentAsString();
        assertTrue(content.contains("Peter"));
        assertTrue(content.contains("Moscow"));
        assertTrue(content.contains("Kirovsk"));
        assertEquals(3, content.split(",").length);
    }

    @Test
    public void testCanSearchForPets() throws Exception {
        MvcResult r = mockMvc.perform(get("/" + SEARCH_PATH)
                .param("address", "Peter")
                .param("gender", "Male")
                .param("purpose", "Breeding")
                .param("type", "Dog")
                .param("minAge", "3")
                .param("maxAge", "6")
        )
                .andExpect(status().isOk())
                .andReturn();
        String content = r.getResponse().getContentAsString();
        assertTrue(content.contains(TEST_PET_ID.toString()));
    }

}