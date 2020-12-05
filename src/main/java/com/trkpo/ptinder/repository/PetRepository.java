package com.trkpo.ptinder.repository;

import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwner(User owner);
}
