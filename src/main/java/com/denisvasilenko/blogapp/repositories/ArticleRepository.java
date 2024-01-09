package com.denisvasilenko.blogapp.repositories;

import com.denisvasilenko.blogapp.models.Article;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {
    Optional<Article> findByNameArticle(String nameArticle);
    List<Article> findAllByNameArticle(String nameArticle);

    @Modifying
    @Query(value = "DELETE FROM articles WHERE id = ?1", nativeQuery = true)
    void deleteById(UUID uuid);
}
