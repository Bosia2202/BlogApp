package com.denisvasilenko.blogapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="articles")
public class Article {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "nameArticle")
    private String nameArticle;
    @Column(name = "url")
    private String url;
    @Column(name = "dateOfCreation")
    private LocalDate dateOfCreation;
    @Column(name = "likes")
    private int likes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    @JsonBackReference
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
                Objects.equals(dateOfCreation, article.dateOfCreation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameArticle, url, dateOfCreation, likes, userOwner);
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
