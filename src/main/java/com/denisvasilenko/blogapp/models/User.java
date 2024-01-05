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
    @Column(name = "avatarImg",columnDefinition = "bytea")
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
                Objects.equals(profileDescription, user.profileDescription);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, username, password, profileDescription);
        result = 31 * result + Arrays.hashCode(avatarImg);
        return result;
    }

    public User duplicatingUser() {
        User duplicateUser = new User();
        duplicateUser.setId(this.id);
        duplicateUser.setUsername(this.username);
        duplicateUser.setPassword(this.password);
        duplicateUser.setAvatarImg(this.avatarImg);
        duplicateUser.setProfileDescription(this.profileDescription);
        duplicateUser.setArticles(this.articles);
        duplicateUser.setRoleCollection(this.roleCollection);
        return duplicateUser;
    }

}
