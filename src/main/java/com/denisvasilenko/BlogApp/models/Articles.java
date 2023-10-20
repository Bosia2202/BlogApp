package com.denisvasilenko.BlogApp.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name ="Articles")
public class Articles {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user")
    private String user;
    @Column(name = "nameArticle")
    private String nameArticle;
    @Column(name = "url")
    private String url;
    @Column(name = "dateOfCreation")
    private String dateOfCreation;
    @Column(name = "likes")
    private int likes;

    public Articles(){}

    public Articles(String user, String nameArticle, String url, String dateOfCreation, int likes) {
        this.user = user;
        this.nameArticle = nameArticle;
        this.url = url;
        this.dateOfCreation = dateOfCreation;
        this.likes = likes;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNameArticle() {
        return nameArticle;
    }

    public void setNameArticle(String nameArticle) {
        this.nameArticle = nameArticle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Articles articles = (Articles) o;
        return likes == articles.likes && Objects.equals(id, articles.id) && Objects.equals(user, articles.user) && Objects.equals(nameArticle, articles.nameArticle) && Objects.equals(url, articles.url) && Objects.equals(dateOfCreation, articles.dateOfCreation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, nameArticle, url, dateOfCreation, likes);
    }
}
