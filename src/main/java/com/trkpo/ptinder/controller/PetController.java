package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.templates.GoggleId;
import com.trkpo.ptinder.entity.templates.PetAndGoogleId;
import com.trkpo.ptinder.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.trkpo.ptinder.config.Constants.PETS_PATH;

@RestController
@RequestMapping(PETS_PATH)
public class PetController {
    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public List<Pet> listAll() {
        return petService.findAllPets();
    }

    @PostMapping
    public Pet addPet(@RequestBody PetAndGoogleId petAndGoogleId) {
        return petService.savePetForUser(petAndGoogleId);
    }

    @DeleteMapping("{petid}")
    public void deletePet(@PathVariable("petid") Pet pet, @RequestBody GoggleId googleId) {
        petService.deleteById(pet, googleId);
    }
}
