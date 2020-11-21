package com.trkpo.ptinder.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "Notifications")
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"})
public class Notifications {
    @Id
    @GeneratedValue
    private Long id;

    private String text;
    private boolean isRead;

//    @ManyToOne(optional = false, cascade = CascadeType.ALL)
//    private User addressee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
