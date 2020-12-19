package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.repository.PetRepository;
import com.trkpo.ptinder.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class FavouriteService {
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    public FavouriteService(UserRepository userRepository, PetRepository petRepository) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }

    public Set<Pet> findFavouriteForUser(String googleId) {
        User user = userRepository.findByGoogleId(googleId);
        return user.getFavouritePets();
    }

    public Set<Long> findFavouriteIdForUser(String googleId) {
        User user = userRepository.findByGoogleId(googleId);
        Set<Pet> petSet = user.getFavouritePets();
        Set<Long> petIdSet = new HashSet<>();
        for (Pet pet : petSet) {
            petIdSet.add(pet.getPetId());
        }
        return petIdSet;
    }

    public Set<User> findUsersForPet(Pet pet) {
        return pet.getUsersLikes();
    }

    public Set<Pet> addToFavouriteForUser(Long petId, String googleId) {
        User user = userRepository.findByGoogleId(googleId);
        Set<Pet> favourites = user.getFavouritePets();
        Pet pet = petRepository.findById(petId).get();
        favourites.add(pet);
        user.setFavouritePets(favourites);
        userRepository.save(user);
        petRepository.save(pet);
        return user.getFavouritePets();
    }

    public Set<Pet> deleteFromFavouriteById(Long petId, String googleId) {
        User user = userRepository.findByGoogleId(googleId);
        Set<Pet> favourites = user.getFavouritePets();
        Pet pet = petRepository.findById(petId).get();
        favourites.remove(pet);
        user.setFavouritePets(favourites);
        userRepository.save(user);
        petRepository.save(pet);
        return user.getFavouritePets();
    }

}
