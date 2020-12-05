package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.entity.templates.GoogleId;
import com.trkpo.ptinder.entity.templates.PetAndGoogleId;
import com.trkpo.ptinder.repository.PetRepository;
import com.trkpo.ptinder.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PetService {
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public PetService(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    public List<Pet> findAllPets() {
        return petRepository.findAll();
    }

    public List<Pet> findPetsForUser(GoogleId googleId) {
        User user = userRepository.findByGoogleId(googleId.getGoogleId());
        return petRepository.findByOwner(user);
    }

    public Pet savePetForUser(PetAndGoogleId petAndGoogleId) {
        User user = getCurrentUser(petAndGoogleId.getGoogleId());
        Pet pet = petAndGoogleId.getPet();
        pet.setOwner(user);
        userRepository.save(user);
        return petRepository.save(pet);
    }

    public void deleteById(Pet pet, GoogleId googleId) {
        User user = getCurrentUser(googleId.getGoogleId());
        userRepository.save(user);
        petRepository.deleteById(pet.getPetId());
        System.out.println("Successfully deleted pet with id " + pet.getPetId() + "for user " + user);
    }

    public User getCurrentUser(String id) {
        return userRepository.findByGoogleId(id);
    }

    public Pet updatePetInfo(PetAndGoogleId petAndGoogleId, Long id) {
        Pet pet = petAndGoogleId.getPet();
        Pet oldPet = petRepository.findById(id).get();
        oldPet.setAge(pet.getAge());
        oldPet.setBreed(pet.getBreed());
        oldPet.setComment(pet.getComment());
        oldPet.setName(pet.getName());
        oldPet.setGender(pet.getGender());
        oldPet.setPurpose(pet.getPurpose());
        return petRepository.save(oldPet);
    }
}
