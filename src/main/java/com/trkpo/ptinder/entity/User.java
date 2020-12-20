package com.trkpo.ptinder.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trkpo.ptinder.entity.enums.Gender;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
@ToString(of = {"firstName", "lastName"})
@EqualsAndHashCode(of = {"googleId"})
@JsonIgnoreProperties("favouritePets")
public class User {
    @Id
    private String googleId;

    private String firstName;
    private String middleName;
    private String lastName;

    private Gender gender;

    private String number;
    private String address;
    private String email;

    private String photoUrl;

    private boolean isContactInfoPublic;

    @OneToMany(mappedBy = "owner",
            cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Pet> pets = new HashSet<>();

//    @OneToMany(mappedBy = "id", fetch = FetchType.EAGER)
//    private List<Notifications> notifications;

    @ManyToMany
    @JoinTable(
            name = "favourite_pets",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "pet_id"))
    private Set<Pet> favouritePets = new HashSet<>();

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isContactInfoPublic() {
        return isContactInfoPublic;
    }

    public void setContactInfoPublic(boolean contactInfoPublic) {
        isContactInfoPublic = contactInfoPublic;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public Set<Pet> getFavouritePets() {
        return favouritePets;
    }

    public void setFavouritePets(Set<Pet> favouritePets) {
        this.favouritePets = favouritePets;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
