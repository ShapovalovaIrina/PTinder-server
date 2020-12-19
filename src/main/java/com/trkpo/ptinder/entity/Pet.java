package com.trkpo.ptinder.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trkpo.ptinder.entity.enums.Gender;
import com.trkpo.ptinder.entity.enums.Purpose;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Pets")
@ToString(of = {"petId", "name"})
@EqualsAndHashCode(of = {"petId"})
@JsonIgnoreProperties("usersLikes")
public class Pet {
    @Id
    @GeneratedValue
    private Long petId;

    private String name;
    private Integer age;
    private Gender gender;

    @ManyToOne(fetch = FetchType.EAGER)
    private AnimalType animalType;

    private String breed;
    private Purpose purpose;
    private String comment;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Photo> petPhotos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany(mappedBy = "favouritePets")
    Set<User> usersLikes = new HashSet<>();

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long id) {
        this.petId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Collection<Photo> getPetPhotos() {
        return petPhotos;
    }

    public void setPetPhotos(Collection<Photo> petPhotos) {
        this.petPhotos.clear();
        if (petPhotos != null) {
            this.petPhotos.addAll(petPhotos);
        }
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setUsersLikes(Set<User> usersLikes) {
        this.usersLikes = usersLikes;
    }

    public Set<User> getUsersLikes() {
        return usersLikes;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }
}
