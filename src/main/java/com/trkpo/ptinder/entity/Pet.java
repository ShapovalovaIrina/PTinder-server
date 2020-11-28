package com.trkpo.ptinder.entity;

import com.trkpo.ptinder.entity.enums.Breed;
import com.trkpo.ptinder.entity.enums.Gender;
import com.trkpo.ptinder.entity.enums.Purpose;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Pets")
@ToString(of = {"petId", "name"})
@EqualsAndHashCode(of = {"petId"})
public class Pet {
    @Id
    @GeneratedValue
    private Long petId;

    private String name;
    private Integer age;
    private Gender gender;
    private Breed breed;
    private Purpose purpose;
    private String comment;

    @OneToMany(mappedBy = "pet", fetch = FetchType.EAGER, orphanRemoval = true)
    private Collection<Photo> petPhotos;

    @ManyToOne
    @JoinColumn(name = "pets")
    private User owner;

    @ManyToMany(mappedBy = "favouritePets")
    List<User> usersLikes;

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

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
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
        this.petPhotos = petPhotos;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setUsersLikes(List<User> usersLikes) {
        this.usersLikes = usersLikes;
    }
}
