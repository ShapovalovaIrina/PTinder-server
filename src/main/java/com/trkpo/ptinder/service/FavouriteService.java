package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.entity.templates.GoogleId;
import com.trkpo.ptinder.repository.PetRepository;
import com.trkpo.ptinder.repository.UserRepository;
import org.springframework.stereotype.Service;

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

    public Set<User> findUsersForPet(Pet pet) {
        return pet.getUsersLikes();
    }

    public Set<Pet> addToFavouriteForUser(Pet pet, GoogleId googleId) {
        User user = userRepository.findByGoogleId(googleId.getGoogleId());
        Set<Pet> favourites = user.getFavouritePets();
        favourites.add(pet);
        user.setFavouritePets(favourites);
        userRepository.save(user);
        petRepository.save(pet);
        return user.getFavouritePets();
    }

    public void deleteFromFavouriteById(Pet pet, GoogleId googleId) {
        User user = userRepository.findByGoogleId(googleId.getGoogleId());
        Set<Pet> favourites = user.getFavouritePets();
        favourites.remove(pet);
        user.setFavouritePets(favourites);
        userRepository.save(user);
        petRepository.save(pet);
    }

}
