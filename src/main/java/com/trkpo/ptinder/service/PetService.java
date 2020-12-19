package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.AnimalType;
import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.Photo;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.entity.enums.Gender;
import com.trkpo.ptinder.entity.enums.Purpose;
import com.trkpo.ptinder.entity.templates.GoogleId;
import com.trkpo.ptinder.entity.templates.PetAndGoogleId;
import com.trkpo.ptinder.entity.templates.SearchInfo;
import com.trkpo.ptinder.repository.AnimalTypeRepository;
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
    private final AnimalTypeRepository animalTypeRepository;

    public PetService(PetRepository petRepository, UserRepository userRepository, PhotoRepository photoRepository, AnimalTypeRepository animalTypeRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
        this.animalTypeRepository = animalTypeRepository;
    }

    public List<Pet> findAllPets() {
        return petRepository.findAll();
    }

    public Optional<Pet> findPet(Long id) {
        return petRepository.findById(id);
    }

    public List<Pet> findPetsForUser(String googleId) {
        User user = userRepository.findByGoogleId(googleId);
        List<Pet> p = petRepository.findByOwner(user);
        System.out.println();
        return petRepository.findByOwner(user);
    }

    public Pet savePetForUser(PetAndGoogleId petAndGoogleId) {
        log.info("Going to save pet for user {}", petAndGoogleId.getGoogleId());
        User user = getCurrentUser(petAndGoogleId.getGoogleId());
        Pet pet = petAndGoogleId.getPet();
        pet.setOwner(user);
        userRepository.save(user);
        AnimalType type = animalTypeRepository.findByType(petAndGoogleId.getType());
        pet.setAnimalType(type);
        animalTypeRepository.save(type);
        pet = petRepository.save(pet);
        return getPet(petAndGoogleId, pet);
    }

    private Pet getPet(PetAndGoogleId petAndGoogleId, Pet pet) {
        List<Photo> photos = petAndGoogleId.getPhotos();
        if (photos != null) {
            for (Photo photo : photos) {
                photo.setPet(pet);
            }
            photoRepository.saveAll(photos);
        }
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
        oldPet.setComment(pet.getComment());
        oldPet.setName(pet.getName());
        oldPet.setGender(pet.getGender());
        oldPet.setPurpose(pet.getPurpose());
        oldPet.setBreed(pet.getBreed());
        return getPet(petAndGoogleId, oldPet);
    }

    public List<Pet> findPetsWithFilters(String addres, String gender, String purpose, String type, String minAge, String maxAge) {
        AnimalType tmpType = animalTypeRepository.findByType(type);
        List<Pet> byType = Collections.EMPTY_LIST;
        if (tmpType != null) {
            byType = petRepository.findByAnimalType(tmpType);
        }
        List<Pet> byGender = petRepository.findByGender(gender.equalsIgnoreCase("male") ? Gender.MALE : Gender.FEMALE);
        List<Pet> byAge = collectAnimalsForAllAges(minAge, maxAge);
        List<Pet> byPurpose = collectByPurpose(purpose);
        List<Pet> byAddress = findByAddress(addres);
        Set<Pet> filteredPets = new HashSet<>();
        filteredPets.addAll(byAddress);
        filteredPets.addAll(byAge);
        filteredPets.addAll(byType);
        filteredPets.addAll(byGender);
        filteredPets.addAll(byPurpose);
        if (!byType.isEmpty()) {
            filteredPets.retainAll(byType);
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

    private List<Pet> collectByPurpose(String purpose) {
        if (purpose.equalsIgnoreCase(Purpose.NOTHING.name())) {
            return petRepository.findAll();
        }
        Purpose p = Purpose.NOTHING;
        if (purpose.equalsIgnoreCase(Purpose.BREEDING.name())) {
            p = Purpose.BREEDING;
        }
        if (purpose.equalsIgnoreCase(Purpose.DONORSHIP.name())) {
            p = Purpose.DONORSHIP;
        }
        if (purpose.equalsIgnoreCase(Purpose.FRIENDSHIP.name())) {
            p = Purpose.FRIENDSHIP;
        }
        if (purpose.equalsIgnoreCase(Purpose.WALKING.name())) {
            p = Purpose.WALKING;
        }
        return petRepository.findByPurpose(p);
    }

    private List<Pet> collectAnimalsForAllAges(String minAge, String maxAge) {
        if (minAge == null || minAge.isEmpty()) {
            minAge = "0";
        }
        if (maxAge == null || maxAge.isEmpty()) {
            maxAge = "1000";
        }
        List<Pet> result = new ArrayList<>();
        for (int i = Integer.parseInt(minAge); i < Integer.parseInt(maxAge); i++) {
            result.addAll(petRepository.findByAge(i));
        }
        return result;
    }

    private List<Pet> findByAddress(String address) {
        if (address.equals("-")) {
            return Collections.emptyList();
        }
        List<User> usersAtAddress = userRepository.findByAddress(address);
        List<Pet> petsAtAddress = new ArrayList<>();
        for (User usr : usersAtAddress) {
            petsAtAddress.addAll(petRepository.findByOwner(usr));
        }
        return petsAtAddress;
    }

    public List<AnimalType> getAllAnimalTypes() {
        return animalTypeRepository.findAll();
    }

    public AnimalType addNewAnimalType(AnimalType type) {
        AnimalType oldType = animalTypeRepository.findByType(type.getType());
        return oldType != null ? oldType : animalTypeRepository.save(type);
    }

    public List<String> getAllAddresses() {
        List<User> allUsers = userRepository.findAll();
        Set<String> addr = new HashSet<>();
        for (User u : allUsers) {
            addr.add(u.getAddress());
        }
        return new ArrayList<>(addr);
    }
}
