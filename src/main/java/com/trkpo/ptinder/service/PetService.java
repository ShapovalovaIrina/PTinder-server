package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.Photo;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.entity.templates.GoogleId;
import com.trkpo.ptinder.entity.templates.PetAndGoogleId;
import com.trkpo.ptinder.entity.templates.SearchInfo;
import com.trkpo.ptinder.repository.PetRepository;
import com.trkpo.ptinder.repository.PhotoRepository;
import com.trkpo.ptinder.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

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
        oldPet.setAnimalType(pet.getAnimalType());
        oldPet.setComment(pet.getComment());
        oldPet.setName(pet.getName());
        oldPet.setGender(pet.getGender());
        oldPet.setPurpose(pet.getPurpose());
        return getPet(petAndGoogleId, oldPet);
    }

    private Pet getPet(PetAndGoogleId petAndGoogleId, Pet oldPet) {
        List<Photo> photos = petAndGoogleId.getPhotos();
        if (photos != null) {
            for (Photo photo : photos) {
                photo.setPet(oldPet);
            }
            photoRepository.saveAll(photos);
        }
        oldPet.setPetPhotos(photos);
        return petRepository.save(oldPet);
    }

    public List<Pet> findPetsWithFilters(SearchInfo info) {
        List<Pet> byType = info.getAnimalType() != null ? petRepository.findByAnimalType(info.getAnimalType()) : Collections.emptyList();
        List<Pet> byBreed = info.getBreed() != null ? petRepository.findByBreed(info.getBreed()) : Collections.emptyList();
        List<Pet> byGender = info.getGender() != null ? petRepository.findByGender(info.getGender()) : Collections.emptyList();
        List<Pet> byAge = info.getAge() != null ? petRepository.findByAge(info.getAge()) : Collections.emptyList();
        List<Pet> byPurpose = info.getPurpose() != null ? petRepository.findByPurpose(info.getPurpose()) : Collections.emptyList();
        List<Pet> byAddress = info.getAddress() != null ? findByAddress(info.getAddress()) : Collections.emptyList();
        Set<Pet> filteredPets = new HashSet<>();
        filteredPets.addAll(byAddress);
        filteredPets.addAll(byAge);
        filteredPets.addAll(byType);
        filteredPets.addAll(byBreed);
        filteredPets.addAll(byGender);
        filteredPets.addAll(byPurpose);
        if (!byType.isEmpty()) {
            filteredPets.retainAll(byType);
        }
        if (!byBreed.isEmpty()) {
            filteredPets.retainAll(byBreed);
        }
        if (!byGender.isEmpty()) {
            filteredPets.retainAll(byGender);
        }
        if (!byAddress.isEmpty()) {
            filteredPets.retainAll(byAddress);
        }
        if (!byAge.isEmpty()) {
            filteredPets.retainAll(byAge);
        }
        if (!byPurpose.isEmpty()) {
            filteredPets.retainAll(byPurpose);
        }
        return new ArrayList<>(filteredPets);
    }

    private List<Pet> findByAddress(String address) {
        List<User> usersAtAddress = userRepository.findByAddress(address);
        List<Pet> petsAtAddress = new ArrayList<>();
        for (User usr : usersAtAddress) {
            petsAtAddress.addAll(petRepository.findByOwner(usr));
        }
        return petsAtAddress;
    }
}
