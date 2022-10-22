package com.example.botdemo.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String photoId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Usser user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserPhoto userPhoto = (UserPhoto) o;
        return id != null && Objects.equals(id, userPhoto.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
