package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.AnimalType;
import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.User;

public class AbstractServiceTest {
    static Pet testPet;
    static User testUser;
    static AnimalType type;

    static final String TEST_GOOGLE_ID = "111";
    static public final Long TEST_PET_ID = 111L;

    public static void initPetAndUser() {
        testPet = new Pet();
        testPet.setPetId(TEST_PET_ID);
        testPet.setName("Doggo");
        type = new AnimalType();
        type.setType("Dog");
        testPet.setAnimalType(type);

        testUser = new User();
        testUser.setGoogleId("101");
        testUser.setFirstName("firstName");
    }
}
