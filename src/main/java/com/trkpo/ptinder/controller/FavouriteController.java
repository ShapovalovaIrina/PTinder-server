package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.service.FavouriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static com.trkpo.ptinder.config.Constants.FAVOURITE_PATH;

@RestController
@RequestMapping(FAVOURITE_PATH)
public class FavouriteController {
    private final FavouriteService favouriteService;

    @Autowired
    public FavouriteController(FavouriteService favouriteService) {
        this.favouriteService = favouriteService;
    }

    @GetMapping("user/full/{googleid}")
    public Set<Pet> getFavouritePets(@PathVariable("googleid") String googleId) {
        return favouriteService.findFavouriteForUser(googleId);
    }

    @GetMapping("user/id/{googleid}")
    public Set<Long> getFavouritePetsId(@PathVariable("googleid") String googleId) {
        return favouriteService.findFavouriteIdForUser(googleId);
    }

    @GetMapping("{petid}")
    public Set<User> getUsersForPet(@PathVariable("petid") Pet pet) {
        return favouriteService.findUsersForPet(pet);
    }

    @PostMapping("{petid}/user/{googleid}")
    public void addFavouritePet(@PathVariable("petid") Long petId, @PathVariable("googleid") String googleId) {
        favouriteService.addToFavouriteForUser(petId, googleId);
    }

    @DeleteMapping("{petid}/user/{googleid}")
    public void deleteFavouritePet(@PathVariable("petid") Long petId, @PathVariable("googleid") String googleId) {
        favouriteService.deleteFromFavouriteById(petId, googleId);
    }
}
