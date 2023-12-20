package com.denisvasilenko.blogapp;

import com.denisvasilenko.blogapp.models.Article;
import com.denisvasilenko.blogapp.models.User;
import com.denisvasilenko.blogapp.repositories.ArticleRepository;
import com.denisvasilenko.blogapp.repositories.UserRepository;
import com.denisvasilenko.blogapp.services.RoleService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenUseMethodFindById_thanShouldGetExpectedArticleAndDeleteIt() {
        Long expectedArticleId = 1L;
        Article expectedArticle = new Article();
        expectedArticle.setId(expectedArticleId);
        articleRepository.save(expectedArticle);
        Article actualArticle = articleRepository.findById(expectedArticleId).orElse(null);
        Assertions.assertEquals(expectedArticle, actualArticle);
        articleRepository.deleteById(expectedArticleId);
    }

    @Test
    public void whenUseMethodFindByIdAndCurrentArticleDoesNotExist_thanShouldGetThrowNotFoundArticleException() {
        Long id = 1L;
        Optional<Article> doesNotExitsArticle = articleRepository.findById(id);
        Assertions.assertFalse(doesNotExitsArticle.isPresent());
    }

    @Test
    public void whenUseMethodFindByArticleName_thanShouldGetExpectedArticle() {
        Long articleId = 1L;
        String articleName = "testArticle";
        String url = "https://blogapp.yandex.cloud/testArticle";
        LocalDate testDate = LocalDate.now();
        int likes = 0;
        User testUser = createEmptyTestUser();
        Article newArticle = new Article(articleId, articleName, url, testDate, likes, testUser);
        articleRepository.save(newArticle);
        User expectedTestUser = createExpectedTestUser();
        Article expectedArticle = new Article(articleId,articleName,url,testDate,likes,expectedTestUser);
        Optional<Article> actualArticleOptional = articleRepository.findByNameArticle(articleName);
        if (actualArticleOptional.isPresent()) {
            Article actualArticle = actualArticleOptional.get();
            Assertions.assertEquals(expectedArticle, actualArticle);
        }
    }

    private User createEmptyTestUser() {
        Long userId = 1L;
        String userName = "testUser";
        String password = "$2a$10$YfmxeAy6/ALyJnWHCScmiuQZnYj/KGW9FQCvpXbso91AnYotRTatm";
        byte[] avatarImg = null;
        String profileDescription = "testDescription";
        User testUser = new User(userId,userName,password,avatarImg,profileDescription,null,null);
        testUser.setRoleCollection(List.of(roleService.getUserRole()));
        userRepository.save(testUser);
        return testUser;
    }
    private User createExpectedTestUser() {
        Long userId = 1L;
        String userName = "testUser";
        String password = "$2a$10$YfmxeAy6/ALyJnWHCScmiuQZnYj/KGW9FQCvpXbso91AnYotRTatm";
        byte[] avatarImg = null;
        String profileDescription = "testDescription";
        Long articleId = 1L;
        String articleName = "testArticle";
        String url = "https://blogapp.yandex.cloud/testArticle";
        LocalDate testDate = LocalDate.now();
        int likes = 0;
        User testUser = new User(userId,userName,password,avatarImg,profileDescription,null,null);
        testUser.setArticles(List.of(new Article(articleId,articleName,url,testDate,likes,testUser)));
        testUser.setRoleCollection(List.of(roleService.getUserRole()));
        return testUser;
    }
}

