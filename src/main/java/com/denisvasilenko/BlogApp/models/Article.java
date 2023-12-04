package com.denisvasilenko.BlogApp.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Date;
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
    private Date dateOfCreation;
    @Column(name = "likes")
    private int likes;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User userOwner;

    @Override
    public String toString() {
        return "Article{" +
                "nameArticle='" + nameArticle + '\'' +
                ", url='" + url + '\'' +
                ", dateOfCreation=" + dateOfCreation +
                ", likes=" + likes +
                ", userOwner=" + userOwner.getUsername() +
                '}';
    }
}
