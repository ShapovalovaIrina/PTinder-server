package com.trkpo.ptinder.entity.templates;

import com.trkpo.ptinder.entity.enums.AnimalType;
import com.trkpo.ptinder.entity.enums.Gender;
import com.trkpo.ptinder.entity.enums.Purpose;

public class SearchInfo {

    private AnimalType animalType;
    private String breed;
    private Gender gender;
    private Integer age;
    private Purpose purpose;
    private String address;

    public AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
