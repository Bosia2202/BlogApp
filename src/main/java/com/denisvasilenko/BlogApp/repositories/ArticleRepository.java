package com.denisvasilenko.BlogApp.repositories;

import com.denisvasilenko.BlogApp.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
    Optional<Article> findByNameArticle(String nameArticle);
}
