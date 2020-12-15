package com.trkpo.ptinder.entity.templates;

import com.trkpo.ptinder.entity.Pet;
import com.trkpo.ptinder.entity.Photo;

import java.util.List;

public class PetAndGoogleId {
    Pet pet;
    String type;
    List<Photo> photos;
    String googleId;

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
