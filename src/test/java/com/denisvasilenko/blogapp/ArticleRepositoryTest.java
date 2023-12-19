package com.denisvasilenko.blogapp;

import com.denisvasilenko.blogapp.models.Article;
import com.denisvasilenko.blogapp.repositories.ArticleRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void whenUseMethodFindById_thanShouldGetExpectedArticle() {
        Long id = 1L;
        Article expectedArticle = new Article();
        expectedArticle.setId(id);
        articleRepository.save(expectedArticle);
        Article actualArticle = articleRepository.findById(id).orElse(null);
        Assertions.assertEquals(expectedArticle, actualArticle);
    }

}
