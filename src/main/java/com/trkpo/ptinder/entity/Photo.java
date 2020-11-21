package com.trkpo.ptinder.entity;

import javax.persistence.*;

@Entity
@Table(name = "Photos")
public class Photo {
    @Id
    @GeneratedValue
    private Long id;

    // TODO how to save photos to pg ???
    private String photo;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Pet pet;
}
