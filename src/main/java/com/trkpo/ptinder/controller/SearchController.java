package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.service.PetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
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
    public List<Pet> listAll(
            @QueryParam("address") String address,
            @QueryParam("gender") String gender,
            @QueryParam("purpose") String purpose,
            @QueryParam("type") String type,
            @QueryParam("minAge") String minAge,
            @QueryParam("maxAge") String maxAge
                             ) {
        return petService.findPetsWithFilters(address, gender, purpose, type, minAge, maxAge);
    }

    @GetMapping("/address")
    public List<String> getAllAddresses() {
        return petService.getAllAddresses();
    }

}
