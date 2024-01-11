package com.denisvasilenko.blogapp.repositories;

import com.denisvasilenko.blogapp.DTO.ArticleDto.CreateArticleDto;
import com.denisvasilenko.blogapp.DTO.RegistrationDto.UserRegistrationRequest;
import com.denisvasilenko.blogapp.exceptions.NotFoundArticleException;
import com.denisvasilenko.blogapp.models.Article;
import com.denisvasilenko.blogapp.models.User;
import com.denisvasilenko.blogapp.services.ArticleService;
import com.denisvasilenko.blogapp.services.ProfileServices;
import com.denisvasilenko.blogapp.services.RoleService;
import com.denisvasilenko.blogapp.yandexCloudStore.DTO.CloudUploadRequest;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;


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
    public void whenUseMethodAddArticle_thanShouldGetExpectedArticleAndDeleteIt() {
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
        profileServices.deleteUser(emptyUser);
    }

    @Test
    public void whenUseMethodFindByIdAndCurrentArticleDoesNotExist_thanShouldGetThrowNotFoundArticleException() {
        UUID doesNotExistArticleUUID = UUID.fromString("FFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF");
        Assertions.assertThrows(NotFoundArticleException.class, () -> articleService.findArticleById(doesNotExistArticleUUID));
    }

    @Test
    public void whenUseMethodAddArticle_thanShouldGetExpectedArticleFromSpecificUserAndDeleteIt() {
        User emptyUser = createEmptyTestUser();
        String username = emptyUser.getUsername();

        String articleNameForFirstArticle = "Car: Nissan Teana L33";
        String articleContentForFirstArticle = "The Nissan Teana is a mid-size sedan produced by Japanese automobile manufacturer Nissan. It was exported as the Nissan Maxima and Nissan Cefiro to certain markets. It replaces the Nissan Bluebird, Laurel and Cefiro.";
        CreateArticleDto createArticleDtoForFirstArticle = new CreateArticleDto(articleNameForFirstArticle,articleContentForFirstArticle);
        articleService.addArticle(username,createArticleDtoForFirstArticle);

        String articleNameForSecondArticle = "Airplane: Boeing 747";
        String articleContentForSecondArticle = "The Boeing 747 is a large, long-range wide-body airliner designed and manufactured by Boeing Commercial Airplanes in the United States between 1968 and 2023. After introducing the 707 in October 1958, Pan Am wanted a jet 2+1⁄2 times its size, to reduce its seat cost by 30%. In 1965, Joe Sutter left the 737 development program to design the 747. ";
        CreateArticleDto createArticleDtoForSecondArticle = new CreateArticleDto(articleNameForSecondArticle,articleContentForSecondArticle);
        articleService.addArticle(username,createArticleDtoForSecondArticle);

        String articleNameForThirdArticle = "Car: Porsche 911";
        String articleContentForThirdArticle = "Porsche 911 — общее название семейства спортивных автомобилей и автомобилей категории GT, выпускающихся компанией немецкой Porsche AG с 1965 года по настоящее время.";
        CreateArticleDto createArticleDtoForThirdArticle = new CreateArticleDto(articleNameForThirdArticle,articleContentForThirdArticle);
        articleService.addArticle(username,createArticleDtoForThirdArticle);

        String[] expectedArticlesNames = new String[]{articleNameForFirstArticle, articleNameForThirdArticle} ;
       // String[] expectedArticlesContent = new String[]{articleContentForFirstArticle,articleContentForThirdArticle};
        List<Article> actualArticles = articleService.searchArticlesByNameArticleFromSpecificUser("car",username);
        IntStream.range(0, actualArticles.size()).forEach(articleIterator ->
                Assertions.assertEquals(expectedArticlesNames[articleIterator], actualArticles.get(articleIterator).getNameArticle())
        );
       List<Article> articlesForDelete = emptyUser.getArticles();
       for(Article article : articlesForDelete) {
           articleService.deleteArticle(username,article.getId());
       }
       profileServices.deleteUser(emptyUser);
       //TODO: Доделать сравнение для контента
    }

    private User createEmptyTestUser() {
        String username = "testUser";
        String password = "12345";
        UserRegistrationRequest userRequest = new UserRegistrationRequest(username, password);
        return profileServices.createUser(userRequest);
    }
}


