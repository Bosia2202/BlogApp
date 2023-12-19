package com.denisvasilenko.blogapp.repositories;

import com.denisvasilenko.blogapp.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
    Optional<Article> findByNameArticle(String nameArticle);
}
