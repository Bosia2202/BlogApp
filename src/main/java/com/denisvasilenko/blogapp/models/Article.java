package com.denisvasilenko.blogapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private LocalDate dateOfCreation;
    @Column(name = "likes")
    private int likes;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User userOwner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return likes == article.likes &&
                Objects.equals(id, article.id) &&
                Objects.equals(nameArticle, article.nameArticle) &&
                Objects.equals(url, article.url) &&
                Objects.equals(dateOfCreation, article.dateOfCreation) &&
                Objects.equals(userOwner, article.userOwner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameArticle, url, dateOfCreation, likes, userOwner);
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Article {" +
                "nameArticle = '" + nameArticle + '\'' +
                ", url = '" + url + '\'' +
                ", dateOfCreation = " + dateOfCreation +
                ", likes = " + likes +
                ", userOwner = " + userOwner.getUsername() +
                '}';
    }
}
