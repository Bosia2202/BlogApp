package com.denisvasilenko.BlogApp.models;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "Users")
public class Users {
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

    public Users(){}

    public Users(String username, byte[] avatarImg, String profileDescription) {
        this.username = username;
        this.avatarImg = avatarImg;
        this.profileDescription = profileDescription;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(username, users.username) && Arrays.equals(avatarImg, users.avatarImg) && Objects.equals(profileDescription, users.profileDescription);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(username, profileDescription);
        result = 31 * result + Arrays.hashCode(avatarImg);
        return result;
    }
}
