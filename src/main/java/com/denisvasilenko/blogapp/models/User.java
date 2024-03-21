package com.denisvasilenko.blogapp.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username",unique = true)
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "profileDescription")
    private String profileDescription;
    @OneToMany(mappedBy = "userOwner", cascade = CascadeType.ALL)
    private List<Article> articles;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable (
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roleCollection;

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return  Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(profileDescription, user.profileDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, profileDescription);
    }

    public User duplicatingUser() {
        User duplicateUser = new User();
        duplicateUser.setId(this.id);
        duplicateUser.setUsername(this.username);
        duplicateUser.setPassword(this.password);
        duplicateUser.setProfileDescription(this.profileDescription);
        duplicateUser.setArticles(this.articles);
        duplicateUser.setRoleCollection(this.roleCollection);
        return duplicateUser;
    }

}
