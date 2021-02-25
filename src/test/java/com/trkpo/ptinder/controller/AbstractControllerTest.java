package com.trkpo.ptinder.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.trkpo.ptinder.entity.AnimalType;
import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.User;

public class AbstractControllerTest {
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
        testUser.setGoogleId(TEST_GOOGLE_ID);
        testUser.setFirstName("firstName");
        testUser.setAddress("Peter");
    }

    String getObjectAsJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(obj);
    }
}
