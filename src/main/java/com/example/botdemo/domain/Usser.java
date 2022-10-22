package com.example.botdemo.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Usser {
    @Id
    private Long id;
    private String usernameTelegram;
    private String name;
    private String details;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "user")
//TODO make fetchtype LAZY and then refactor repo methods
    @ToString.Exclude
    private List<UserPhoto> photos = new ArrayList<>();

    private String city;
    private String gender;
    private int age;

    private String preferredGender;
    private int desiredLowerAgeLimit;
    private int desiredUpperAgeLimit;

    @Transient
    private UserState state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Usser usser = (Usser) o;
        return id != null && Objects.equals(id, usser.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void cleanPhotos() {
        this.photos = new ArrayList<>();
    }

}
