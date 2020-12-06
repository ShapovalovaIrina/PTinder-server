package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.Photo;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.entity.templates.GoogleId;
import com.trkpo.ptinder.entity.templates.PetAndGoogleId;
import com.trkpo.ptinder.repository.PetRepository;
import com.trkpo.ptinder.repository.PhotoRepository;
import com.trkpo.ptinder.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PetService {
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    public PetService(PetRepository petRepository, UserRepository userRepository, PhotoRepository photoRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
    }

    public List<Pet> findAllPets() {
        return petRepository.findAll();
    }

    public Optional<Pet> findPet(Long id) {
        return petRepository.findById(id);
    }

    public List<Pet> findPetsForUser(String googleId) {
        User user = userRepository.findByGoogleId(googleId);
        return petRepository.findByOwner(user);
    }

    public Pet savePetForUser(PetAndGoogleId petAndGoogleId) {
        User user = getCurrentUser(petAndGoogleId.getGoogleId());
        Pet pet = petAndGoogleId.getPet();
        pet.setOwner(user);
        userRepository.save(user);
        pet = petRepository.save(pet);
        List<Photo> photos = petAndGoogleId.getPhotos();
        for (Photo photo : photos) {
            photo.setPet(pet);
        }
//        List<Photo> photos = petAndGoogleId.getPhotos().stream().map(x -> x.setId(pet)).collect(Collectors.toList());
        photoRepository.saveAll(photos);
        pet.setPetPhotos(photos);
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
        List<Photo> photos = petAndGoogleId.getPhotos();
        for (Photo photo : photos) {
            photo.setPet(oldPet);
        }
        photoRepository.saveAll(photos);
        oldPet.setPetPhotos(photos);
        return petRepository.save(oldPet);
    }
}
