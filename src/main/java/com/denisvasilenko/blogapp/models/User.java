package com.denisvasilenko.blogapp.models;

import com.amazonaws.services.appstream.model.Session;
import com.denisvasilenko.blogapp.repositories.UserRepository;
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
    @Lob
    @Column(name = "avatarImg")
    private byte[] avatarImg;
    @Column(name = "profileDescription")
    private String profileDescription;
    @OneToMany(mappedBy = "userOwner", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Article> articles;
    @ManyToMany
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
                Arrays.equals(avatarImg, user.avatarImg) &&
                Objects.equals(profileDescription, user.profileDescription) &&
                Objects.equals(articles, user.articles) &&
                Objects.equals(roleCollection, user.roleCollection);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, username, password, profileDescription, articles, roleCollection);
        result = 31 * result + Arrays.hashCode(avatarImg);
        return result;
    }

}
