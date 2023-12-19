package com.denisvasilenko.blogapp.models;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Date;
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
