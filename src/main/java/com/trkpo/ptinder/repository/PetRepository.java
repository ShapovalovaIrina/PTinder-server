package com.trkpo.ptinder.repository;

import com.trkpo.ptinder.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
