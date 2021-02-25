package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.entity.AnimalType;
import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.templates.PetAndGoogleId;
import com.trkpo.ptinder.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.trkpo.ptinder.config.Constants.PETS_PATH;

@RestController
@RequestMapping(PETS_PATH)
public class PetController {
    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("{id}")
    public Pet getPet(@PathVariable("id") Long id) {
        Optional<Pet> currentPet = petService.findPet(id);
        return currentPet.orElseGet(Pet::new);
    }

    @GetMapping("owner/{googleid}")
    public List<Pet> listPetsForUser(@PathVariable("googleid") String googleId) {
        return petService.findPetsForUser(googleId);
    }

    @PostMapping
    public Pet addPet(@RequestBody PetAndGoogleId petAndGoogleId) {
        return petService.savePetForUser(petAndGoogleId);
    }

    @DeleteMapping("{petid}")
    public void deletePet(@PathVariable("petid") Pet pet) {
        // TODO there is np test for this case
        petService.deleteById(pet);
    }

    @PutMapping("{petid}")
    public Pet updateInfo(@PathVariable("petid") Long id, @RequestBody PetAndGoogleId petAndGoogleId) {
        return petService.updatePetInfo(petAndGoogleId, id);
    }

    @GetMapping("/types")
    public List<AnimalType> getAvailableAnimalTypes() {
        return petService.getAllAnimalTypes();
    }

    @PostMapping("/types")
    public AnimalType addNewType(@RequestBody AnimalType newType) {
        return petService.addNewAnimalType(newType);
    }
}
