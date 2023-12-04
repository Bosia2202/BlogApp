package com.denisvasilenko.BlogApp.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
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
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roleCollection;
}
