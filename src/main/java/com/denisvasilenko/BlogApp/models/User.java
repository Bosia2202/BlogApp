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
    @Lob
    @Column(name = "avatarImg")
    private byte[] avatarImg;
    @Column(name = "profileDescription")
    private String profileDescription;

    @OneToMany(mappedBy = "userOwner", cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    private List<Follower> followers;

    @OneToMany(mappedBy = "userOwner", cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    private List<Article> articles;
    @OneToMany(mappedBy = "userOwner",cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    private List<Subscription> subscriptions;
    public User(){}

    public User(String username, byte[] avatarImg, String profileDescription) {
        this.username = username;
        this.avatarImg = avatarImg;
        this.profileDescription = profileDescription;
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

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Arrays.equals(avatarImg, user.avatarImg) && Objects.equals(profileDescription, user.profileDescription) && Objects.equals(followers, user.followers) && Objects.equals(articles, user.articles) && Objects.equals(subscriptions, user.subscriptions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, username, profileDescription, followers, articles, subscriptions);
        result = 31 * result + Arrays.hashCode(avatarImg);
        return result;
    }
}
