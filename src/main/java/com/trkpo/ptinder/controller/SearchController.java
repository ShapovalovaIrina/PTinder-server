package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.templates.SearchInfo;
import com.trkpo.ptinder.service.PetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.trkpo.ptinder.config.Constants.SEARCH_PATH;

@RestController
@RequestMapping(SEARCH_PATH)
public class SearchController {
    public final PetService petService;

    public SearchController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public List<Pet> listAll(@RequestBody SearchInfo info) {
        return petService.findPetsWithFilters(info);
    }

}
