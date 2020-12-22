package com.trkpo.ptinder.entity;

import com.trkpo.ptinder.entity.enums.NotificationType;
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

    private NotificationType type;
    private String text;
    private boolean isRead;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User addressee;

    public Notifications(String text, NotificationType type, User u) {
        this.text = text;
        this.type = type;
        this.addressee = u;
        this.isRead = false;
    }

    public Notifications() {

    }

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

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public User getAddressee() {
        return addressee;
    }

    public void setAddressee(User addressee) {
        this.addressee = addressee;
    }
}
