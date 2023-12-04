package com.denisvasilenko.BlogApp;

import com.denisvasilenko.BlogApp.DTO.ArticleDto.ArticleDto;
import com.denisvasilenko.BlogApp.DTO.ArticleDto.CreateArticleDto;
import com.denisvasilenko.BlogApp.repositories.ArticleRepository;
import com.denisvasilenko.BlogApp.services.ArticleService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ArticleTest {

    @Autowired
    private ArticleService articleService;

    @Test
    public void whenDeleteByIdCalled_thenArticleShouldBeDeletedFromDatabase(){
        final String username = "Poster";
        final String articleName = "TestArticle";
        final String articleText = "Test text";
        articleService.addArticle("Poster", new CreateArticleDto(articleName,articleText));
        articleService.deleteArticle("Poster","Poster","TestArticle");
        ArticleDto actuallArticleDto = articleService.showArticle(username,articleName);
        Assertions.assertNull(actuallArticleDto);
    }
}
