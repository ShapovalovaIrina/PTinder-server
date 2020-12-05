package com.trkpo.ptinder.entity;

import javax.persistence.*;

@Entity
@Table(name = "Photos")
public class Photo {
    @Id
    @GeneratedValue
    private Long id;

    @Lob
    private byte[] photo;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Pet pet;
}
