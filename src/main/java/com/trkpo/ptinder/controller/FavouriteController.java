package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.entity.templates.GoogleId;
import com.trkpo.ptinder.entity.templates.PetAndGoogleId;
import com.trkpo.ptinder.service.FavouriteService;
import com.trkpo.ptinder.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.trkpo.ptinder.config.Constants.FAVOURITE_PATH;
import static com.trkpo.ptinder.config.Constants.PETS_PATH;

@RestController
@RequestMapping(FAVOURITE_PATH)
public class FavouriteController {
    private final FavouriteService favouriteService;

    @Autowired
    public FavouriteController(FavouriteService favouriteService) {
        this.favouriteService = favouriteService;
    }

    @GetMapping("user/{googleid}")
    public Set<Pet> getFavouritePets(@PathVariable("googleid") String googleId) {
        return favouriteService.findFavouriteForUser(googleId);
    }

    @GetMapping("{petid}")
    public Set<User> getUsersForPet(@PathVariable("petid") Pet pet) {
        return favouriteService.findUsersForPet(pet);
    }

    @PostMapping("{petid}")
    public Set<Pet> addFavouritePet(@PathVariable("petid") Pet pet, @RequestBody GoogleId googleId) {
        return favouriteService.addToFavouriteForUser(pet, googleId);
    }

    @DeleteMapping("{petid}")
    public void deleteFavouritePet(@PathVariable("petid") Pet pet, @RequestBody GoogleId googleId) {
        favouriteService.deleteFromFavouriteById(pet, googleId);
    }
}
