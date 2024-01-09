package com.denisvasilenko.blogapp.repositories;

import com.denisvasilenko.blogapp.DTO.ArticleDto.CreateArticleDto;
import com.denisvasilenko.blogapp.DTO.RegistrationDto.UserRegistrationRequest;
import com.denisvasilenko.blogapp.models.Article;
import com.denisvasilenko.blogapp.models.User;
import com.denisvasilenko.blogapp.services.ArticleService;
import com.denisvasilenko.blogapp.services.ProfileServices;
import com.denisvasilenko.blogapp.services.RoleService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ArticleServiceTest {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ProfileServices profileServices;

    @Test
    public void whenUseMethodAddArticle_thanShouldGetExpectedArticleFromSpecificUserAndDeleteIt() {
        User emptyUser = createEmptyTestUser();
        String testArticleName = "testArticle";
        String testArticleContent = "\n" +
                "What is Lorem Ipsum?\n" +
                "\n" +
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry." +
                " Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. " +
                "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged." +
                " It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n";
        CreateArticleDto createArticleDto = new CreateArticleDto(testArticleName, testArticleContent);
        articleService.addArticle(emptyUser.getUsername(), createArticleDto);
        Article actualArticle = articleService.findByArticleName(testArticleName);
        Assertions.assertEquals(testArticleName, actualArticle.getNameArticle());
        String actualTestArticleContent = articleService.showArticle(actualArticle).text();
        Assertions.assertEquals(testArticleContent, actualTestArticleContent);
        String actualUserOwnerForArticle = actualArticle.getUserOwner().getUsername();
        Assertions.assertEquals(emptyUser.getUsername(), actualUserOwnerForArticle);
        articleService.deleteArticle(emptyUser.getUsername(),actualArticle.getId());
    }

//    @Test
//    public void whenUseMethodFindByIdAndCurrentArticleDoesNotExist_thanShouldGetThrowNotFoundArticleException() {
//        Long id = 1L;
//        Optional<Article> doesNotExitsArticle = articleRepository.findById(id);
//        Assertions.assertFalse(doesNotExitsArticle.isPresent());
//    }
//
//    @Test
//    public void whenUseMethodFindByArticleName_thanShouldGetExpectedArticle() {
//        Long articleId = 1L;
//        String articleName = "testArticle";
//        String url = "https://blogapp.yandex.cloud/testArticle";
//        LocalDate testDate = LocalDate.now();
//        int likes = 0;
//        User testUser = createEmptyTestUser();
//        Article newArticle = new Article(articleId, articleName, url, testDate, likes, testUser);
//        articleRepository.save(newArticle);
//        User expectedTestUser = createExpectedTestUser();
//        Article expectedArticle = new Article(articleId, articleName, url, testDate, likes, expectedTestUser);
//        Optional<Article> actualArticleOptional = articleRepository.findByNameArticle(articleName);
//        if (actualArticleOptional.isPresent()) {
//            Article actualArticle = actualArticleOptional.get();
//            Assertions.assertEquals(expectedArticle, actualArticle);
//        }
//    }
//
    private User createEmptyTestUser() {
        String username = "testUser";
        String password = "12345";
        UserRegistrationRequest userRequest = new UserRegistrationRequest(username, password);
        return profileServices.createUser(userRequest);
    }
}

