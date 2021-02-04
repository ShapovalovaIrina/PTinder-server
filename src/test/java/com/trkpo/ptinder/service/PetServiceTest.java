package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.AnimalType;
import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.Photo;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.entity.enums.Gender;
import com.trkpo.ptinder.entity.enums.Purpose;
import com.trkpo.ptinder.entity.templates.PetAndGoogleId;
import com.trkpo.ptinder.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static java.util.Optional.ofNullable;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PetServiceTest {
    PetRepository petRepository;
    UserRepository userRepository;
    PhotoRepository photoRepository;
    AnimalTypeRepository animalTypeRepository;
    NotificationsRepository notificationsRepository;

    PetService petService;
    Pet testPet;
    User testUser;
    AnimalType type;

    public final Long TEST_PET_ID = 111L;

    @Before
    public void before() {
        petRepository = Mockito.mock(PetRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        photoRepository = Mockito.mock(PhotoRepository.class);
        animalTypeRepository = Mockito.mock(AnimalTypeRepository.class);
        notificationsRepository = Mockito.mock(NotificationsRepository.class);

        petService = new PetService(petRepository, userRepository, photoRepository, animalTypeRepository, notificationsRepository);
        testPet = new Pet();
        testPet.setPetId(TEST_PET_ID);
        testPet.setName("Doggo");
        type = new AnimalType();
        type.setType("Dog");
        testPet.setAnimalType(type);

        testUser = new User();
        testUser.setGoogleId("101");
        testUser.setFirstName("firstName");
        testUser.setSubscriptions(new HashSet<>());
        Set<User> userSet = new HashSet<>();
        userSet.add(new User());
        testUser.setSubscribers(userSet);
        testUser.setAddress("Peter");

        Set<User> setWithUsers = new HashSet<>();
        setWithUsers.add(testUser);
        testPet.setUsersLikes(setWithUsers);

        when(petRepository.findAll()).thenReturn(Collections.singletonList(testPet));
        when(petRepository.findById(TEST_PET_ID)).thenReturn(ofNullable(testPet));
        when(userRepository.findByGoogleId("101")).thenReturn(testUser);
        when(petRepository.findByOwner(testUser)).thenReturn(Collections.singletonList(testPet));
        when(petRepository.save(testPet)).thenReturn(testPet);
        when(userRepository.findAll()).thenReturn(Collections.singletonList(testUser));
        when(animalTypeRepository.findAll()).thenReturn(Collections.singletonList(type));
        when(animalTypeRepository.findByType("Dog")).thenReturn(type);
    }

    @Test
    public void testAbilityToFindAllPets() {
        assertEquals(Collections.singletonList(testPet), petService.findAllPets());
    }

    @Test
    public void testCanGetPetById() {
        assertTrue(petService.findPet(TEST_PET_ID).isPresent());
        assertFalse(petService.findPet(123L).isPresent());
    }

    @Test
    public void testAbilityToFindUserForUser() {
        assertEquals(Collections.singletonList(testPet), petService.findPetsForUser("101"));
    }

    @Test
    public void cabeGetCurrentUser() {
        assertEquals("firstName", petService.getCurrentUser("101").getFirstName());
    }

    @Test
    public void testAbilityToSavePetForUser() {
        Photo photo = new Photo();
        photo.setPhoto("test_photo");
        PetAndGoogleId petAndGoogleId = new PetAndGoogleId();
        petAndGoogleId.setPet(testPet);
        petAndGoogleId.setGoogleId("101");
        petAndGoogleId.setType("Dog");
        petAndGoogleId.setPhotos(Collections.singletonList(photo));

        petService.savePetForUser(petAndGoogleId);
        verify(userRepository).save(any());
        verify(animalTypeRepository).save(any());
        verify(petRepository, times(2)).save(any());
        assertEquals(testUser, testPet.getOwner());
        assertEquals(Collections.singletonList(photo), testPet.getPetPhotos());
        assertEquals(testPet, photo.getPet());
    }

    @Test
    public void testAbilityToDelete() {
        petService.deleteById(testPet);
        verify(petRepository).deleteById(testPet.getPetId());
    }

    @Test
    public void testAbilityToUpdatePet() {
        Photo photo = new Photo();
        photo.setPhoto("test_photo");
        PetAndGoogleId petAndGoogleId = new PetAndGoogleId();
        Pet tmpPet = new Pet();
        tmpPet.setPetId(TEST_PET_ID);
        tmpPet.setName("aaa");

        petAndGoogleId.setPet(tmpPet);
        petAndGoogleId.setGoogleId("101");
        petAndGoogleId.setType("Dog");
        petAndGoogleId.setPhotos(Collections.singletonList(photo));

        assertEquals("Doggo", petService.findPet(111L).get().getName());
        petService.updatePetInfo(petAndGoogleId, 111L);
        assertEquals("aaa", testPet.getName());
    }

    @Test
    public void testAbilityToGetAllAddresses() {
        assertEquals(Collections.singletonList("Peter"), petService.getAllAddresses());
    }

    @Test
    public void testGetAllAnimalTypes() {
        assertEquals(Collections.singletonList(type), petService.getAllAnimalTypes());
    }

    @Test
    public void testThatNewAnimalTypeWilNotBeAdded() {
        assertEquals(type, petService.addNewAnimalType(type));
        verify(animalTypeRepository, times(0)).save(any());
    }

    @Test
    public void testAbilityToAddNewAnimalType() {
        AnimalType newT = new AnimalType();
        newT.setType("Cat");
        petService.addNewAnimalType(newT);
        verify(animalTypeRepository, times(1)).save(any());
    }

    @Test
    public void testEmptySearchFilter() {
        List<Pet> listOfPets = new ArrayList<>();
        Pet fPet = new Pet();
        listOfPets.add(fPet);
        when(petRepository.findAll()).thenReturn(Arrays.asList(new Pet(), fPet, testPet));
        assertEquals(3, petService.findPetsWithFilters("-", "", "NOTHING", "-", "", "").size());
        assertTrue(petService.findPetsWithFilters("-", "", "NOTHING", "-", "", "").contains(testPet));
    }

    @Test
    public void testFullSearchFilter() {
        List<Pet> listOfPets = new ArrayList<>();
        Pet fPet = new Pet();
        fPet.setName("name");
        fPet.setPetId(123L);
        fPet.setAge(3);
        fPet.setBreed("Katty");
        fPet.setGender(Gender.FEMALE);
        fPet.setPurpose(Purpose.BREEDING);
        fPet.setAnimalType(type);
        listOfPets.add(fPet);
        when(petRepository.findAll()).thenReturn(Arrays.asList(new Pet(), fPet, testPet));
        when(petRepository.findByAnimalType(type)).thenReturn(Collections.singletonList(fPet));
        when(userRepository.findByAddress("Peter")).thenReturn(Collections.singletonList(testUser));
        when(petRepository.findByOwner(testUser)).thenReturn(Collections.singletonList(fPet));
        assertEquals(1, petService.findPetsWithFilters("Peter", "FEMALE", "BREEDING", "Dog", "1", "5").size());
        assertTrue(petService.findPetsWithFilters("Peter", "FEMALE", "BREEDING", "Dog", "1", "5").contains(fPet));
    }

    @Test
    public void testFindPetsWithoutAddress() {
        List<Pet> listOfPets = new ArrayList<>();
        Pet fPet = new Pet();
        fPet.setName("name");
        fPet.setPetId(123L);
        fPet.setAge(3);
        fPet.setBreed("Katty");
        fPet.setGender(Gender.FEMALE);
        fPet.setPurpose(Purpose.BREEDING);
        fPet.setAnimalType(type);
        listOfPets.add(fPet);
        when(petRepository.findAll()).thenReturn(Arrays.asList(new Pet(), fPet, testPet));
        when(petRepository.findByAnimalType(type)).thenReturn(Collections.singletonList(fPet));
        assertEquals(1, petService.findPetsWithFilters("-", "FEMALE", "BREEDING", "Dog", "1", "5").size());
        assertTrue(petService.findPetsWithFilters("-", "FEMALE", "BREEDING", "Dog", "1", "5").contains(fPet));
    }

}