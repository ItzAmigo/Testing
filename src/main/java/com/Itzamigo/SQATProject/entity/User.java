package com.Itzamigo.SQATProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.Objects;

@Builder
@ToString
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname")
    private String nickname;
    @Column(name = "email")
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email == user.email && id.equals(user.id) && nickname.equals(user.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nickname, email);
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }


    public String getEmail() {
        return email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNickname(String firstName) {
        this.nickname = firstName;
    }


    public void setEmail(String email) {
        this.email = email;
    }
}