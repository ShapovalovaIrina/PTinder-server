package com.trkpo.ptinder.entity;

import com.trkpo.ptinder.entity.enums.Gender;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Users")
@ToString(of = {"userId", "firstName", "lastName"})
@EqualsAndHashCode(of = {"userId"})
public class User {
    @Id
    @GeneratedValue
    private Long userId;

    private String firstName;
    private String middleName;
    private String lastName;

    private Gender gender;

    private String number;
    private String address;
    private String email;

    private boolean isContactInfoPublic;

    @OneToMany(mappedBy = "petId", fetch = FetchType.EAGER)
    private List<Pet> userPets;

//    @OneToMany(mappedBy = "id", fetch = FetchType.EAGER)
//    private List<Notifications> notifications;

    @ManyToMany
    @JoinTable(
            name = "favourite_pets",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "pet_id"))
    List<Pet> favouritePets;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

}
