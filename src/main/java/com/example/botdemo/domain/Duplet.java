package com.example.botdemo.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Duplet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne//TODO make fetchtype LAZY and then refactor repo methods
    @JoinColumn(name = "first_user_id")
    private Usser firstUsser;
    @Enumerated(value = EnumType.STRING)
    private UserReact firstUserReact;
    @ManyToOne//TODO make fetchtype LAZY and then refactor repo methods
    @JoinColumn(name = "second_user_id")
    private Usser secondUsser;
    @Enumerated(value = EnumType.STRING)
    private UserReact secondUserReact;
    @Enumerated(value = EnumType.STRING)
    private DupletStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Duplet duplet = (Duplet) o;
        return id != null && Objects.equals(id, duplet.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
