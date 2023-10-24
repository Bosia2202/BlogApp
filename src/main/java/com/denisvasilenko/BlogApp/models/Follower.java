package com.denisvasilenko.BlogApp.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "followers")
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    @Column(name = "follower")
    String follower;

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id",referencedColumnName = "id")
    private User userOwner;

    public Follower(String follower) {
        this.follower = follower;
    }
    public Follower(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public User getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(User userOwner) {
        this.userOwner = userOwner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Follower follower1 = (Follower) o;
        return id == follower1.id && Objects.equals(follower, follower1.follower) && Objects.equals(userOwner, follower1.userOwner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, follower, userOwner);
    }
}
