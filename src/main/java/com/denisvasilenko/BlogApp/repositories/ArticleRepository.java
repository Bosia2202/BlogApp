package com.denisvasilenko.BlogApp.repositories;

import com.denisvasilenko.BlogApp.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
}
