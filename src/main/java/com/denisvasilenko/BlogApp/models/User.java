package com.denisvasilenko.BlogApp.models;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @OneToMany(mappedBy = "userOwner", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Article> articles;

    public User(){}

    public User(String username, byte[] avatarImg, String profileDescription, String password, List<Article> articles) {
        this.username = username;
        this.avatarImg = avatarImg;
        this.profileDescription = profileDescription;
        this.password = password;
        this.articles = articles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getAvatarImg() {
        return avatarImg;
    }

    public void setAvatarImg(byte[] avatarImg) {
        this.avatarImg = avatarImg;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }


    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Arrays.equals(avatarImg, user.avatarImg) && Objects.equals(profileDescription, user.profileDescription) && Objects.equals(articles, user.articles);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, username, password, profileDescription, articles);
        result = 31 * result + Arrays.hashCode(avatarImg);
        return result;
    }
}
