package com.denisvasilenko.BlogApp.models;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name ="articles")
public class Article {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nameArticle")
    private String nameArticle;
    @Column(name = "url")
    private String url;
    @Column(name = "dateOfCreation")
    private Date dateOfCreation;
    @Column(name = "likes")

    private int likes;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User userOwner;

    public Article(){}

    public Article(String nameArticle, String url, Date dateOfCreation, int likes) {
        this.nameArticle = nameArticle;
        this.url = url;
        this.dateOfCreation = dateOfCreation;
        this.likes = likes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public User getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(User userOwner) {
        this.userOwner = userOwner;
    }

    public String getUserName() {
        return userOwner.getUsername();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return likes == article.likes && Objects.equals(id, article.id) && Objects.equals(nameArticle, article.nameArticle) && Objects.equals(url, article.url) && Objects.equals(dateOfCreation, article.dateOfCreation) && Objects.equals(userOwner, article.userOwner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameArticle, url, dateOfCreation, likes, userOwner);
    }
}
