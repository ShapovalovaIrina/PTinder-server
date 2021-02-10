package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.repository.PetRepository;
import com.trkpo.ptinder.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FavouriteServiceTest extends AbstractServiceTest {
    PetRepository petRepository;
    UserRepository userRepository;

    FavouriteService favouriteService;


    @Before
    public void init() {
        initPetAndUser();
        petRepository = Mockito.mock(PetRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        favouriteService = new FavouriteService(userRepository, petRepository);

        Set<Pet> favourites = new HashSet<>();
        favourites.add(testPet);

        testUser.setFavouritePets(favourites);

        Set<User> userLikes = new HashSet<>();
        userLikes.add(testUser);
        testPet.setUsersLikes(userLikes);

        when(userRepository.findByGoogleId(TEST_GOOGLE_ID)).thenReturn(testUser);
        when(petRepository.findById(TEST_PET_ID)).thenReturn(java.util.Optional.of(testPet));
    }

    @Test
    public void testCanGetUsersFavourites() {
        assertTrue(favouriteService.findFavouriteForUser(TEST_GOOGLE_ID).contains(testPet));
        assertEquals(1, favouriteService.findFavouriteForUser(TEST_GOOGLE_ID).size());
    }

    @Test
    public void testCanGetPetsIdsForUser() {
        assertEquals(Collections.singleton(TEST_PET_ID), favouriteService.findFavouriteIdForUser(TEST_GOOGLE_ID));
    }

    @Test
    public void testGetEmptySetWhenUserDontHaveFavourites() {
        when(userRepository.findByGoogleId(any())).thenReturn(new User());
        assertEquals(Collections.emptySet(), favouriteService.findFavouriteIdForUser("000"));
    }

    @Test
    public void testCanGetAllUsersForPet() {
        assertTrue(favouriteService.findUsersForPet(testPet).contains(testUser));
    }

    @Test
    public void testGetEmptySetWhenNobodyLikesPet() {
        assertEquals(Collections.emptySet(), favouriteService.findUsersForPet(new Pet()));
    }

    @Test
    public void testCanAddNewFavouritePetToUser() {
        Pet newPet = new Pet();
        newPet.setPetId(123L);
        when(petRepository.findById(any())).thenReturn(java.util.Optional.of(newPet));

        Set<Pet> favPets = favouriteService.addToFavouriteForUser(123L, TEST_GOOGLE_ID);
        verify(userRepository).save(any());
        verify(petRepository).save(any());

        assertEquals(2, favPets.size());
        assertTrue(favPets.contains(newPet));
    }

    @Test
    public void testCanDeleteFromFavouritePets() {
        Pet newPet = new Pet();
        newPet.setPetId(123L);
        Set<Pet> favourites = new HashSet<>();
        favourites.add(testPet);
        favourites.add(newPet);
        testUser.setFavouritePets(favourites);

        Set<Pet> favPets = favouriteService.deleteFromFavouriteById(TEST_PET_ID, TEST_GOOGLE_ID);
        verify(userRepository).save(any());
        verify(petRepository).save(any());

        assertEquals(1, favPets.size());
        assertTrue(favPets.contains(newPet));
        assertFalse(favPets.contains(testPet));
    }


}