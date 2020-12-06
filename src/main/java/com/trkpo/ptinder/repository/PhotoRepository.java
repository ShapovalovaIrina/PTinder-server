package com.trkpo.ptinder.repository;

import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByPet(Pet pet);
}
