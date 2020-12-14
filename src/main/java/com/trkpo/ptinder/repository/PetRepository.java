package com.trkpo.ptinder.repository;

import com.trkpo.ptinder.entity.AnimalType;
import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.entity.enums.Gender;
import com.trkpo.ptinder.entity.enums.Purpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwner(User owner);

    List<Pet> findByAnimalType(AnimalType type);

    List<Pet> findByBreed(String breed);

    List<Pet> findByGender(Gender gender);

    List<Pet> findByAge(Integer age);

    List<Pet> findByPurpose(Purpose purpose);

}
