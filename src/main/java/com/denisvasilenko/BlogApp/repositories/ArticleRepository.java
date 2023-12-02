package com.denisvasilenko.BlogApp.repositories;

import com.denisvasilenko.BlogApp.models.Article;
import com.denisvasilenko.BlogApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
    Optional<Article> findByNameArticle(String nameArticle);
    void deleteArticleByNameArticle(String articleName);
}
