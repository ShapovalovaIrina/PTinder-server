package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.entity.templates.GoggleId;
import com.trkpo.ptinder.entity.templates.PetAndGoogleId;
import com.trkpo.ptinder.repository.PetRepository;
import com.trkpo.ptinder.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
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

    public Pet savePetForUser(PetAndGoogleId petAndGoogleId) {
        User user = getCurrentUser(petAndGoogleId.getGoogleId());
        Collection<Pet> pets = user.getPets();
        Pet pet = petAndGoogleId.getPet();
        pet.setOwner(user);
        pets.add(pet);
        user.setPets(pets);
        userRepository.save(user);
        return petRepository.save(pet);
    }

    public void deleteById(Pet pet, GoggleId googleId) {
        User user = getCurrentUser(googleId.getGoogleId());
        user.getPets().remove(pet);
        userRepository.save(user);
        petRepository.deleteById(pet.getPetId());
        System.out.println("Successfully deleted pet with id " + googleId.getGoogleId() + "for user " + user);
    }

    public User getCurrentUser(String id) {
        return userRepository.findByGoogleId(id);
    }
}
