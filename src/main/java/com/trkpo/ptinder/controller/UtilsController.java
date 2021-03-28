package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.service.PetService;
import com.trkpo.ptinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.trkpo.ptinder.config.Constants.UTILS_PATH;

@RestController
@RequestMapping(UTILS_PATH)
public class UtilsController {
    private final PetService petService;
    private final UserService userService;

    @Autowired
    public UtilsController(PetService petService, UserService userService) {
        this.petService = petService;
        this.userService = userService;
    }

    @DeleteMapping("/pets")
    public void deleteAllPetsFromDb() {
        petService.deleteAllPets();
    }

    @DeleteMapping("/users")
    public void deleteAllUsersFromDb() {
        userService.deleteAllUsers();
    }



}
