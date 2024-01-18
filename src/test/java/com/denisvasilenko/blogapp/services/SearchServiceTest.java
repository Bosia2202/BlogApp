package com.denisvasilenko.blogapp.services;

import com.denisvasilenko.blogapp.DTO.ArticleDto.CreateArticleDto;
import com.denisvasilenko.blogapp.DTO.RegistrationDto.UserRegistrationRequest;
import com.denisvasilenko.blogapp.DTO.Search.ArticleDtoSearchPreview;
import com.denisvasilenko.blogapp.DTO.Search.SearchQueryDto;
import com.denisvasilenko.blogapp.DTO.Search.UserDtoSearchPreview;
import com.denisvasilenko.blogapp.models.Article;
import com.denisvasilenko.blogapp.models.User;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Log4j2
public class SearchServiceTest {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ProfileServices profileServices;
    @Autowired
    private SearchService searchService;

    @SuppressWarnings("unchecked")
    @Test
    public void whenUseSingleKeywordSearchMethodToSearchForArticle_thanShouldGetExpectedArticles() {
        short ARTICLE_FILTER_ID = 0;
        User user = createEmptyTestUser();
        String username = user.getUsername();

        String articleNameForFirstArticle = "Car: Nissan Teana L33";
        String articleContentForFirstArticle = "The Nissan Teana is a mid-size sedan produced by Japanese automobile manufacturer Nissan. It was exported as the Nissan Maxima and Nissan Cefiro to certain markets. It replaces the Nissan Bluebird, Laurel and Cefiro.";
        CreateArticleDto createArticleDtoForFirstArticle = new CreateArticleDto(articleNameForFirstArticle, articleContentForFirstArticle);
        articleService.addArticle(username, createArticleDtoForFirstArticle);

        String articleNameForSecondArticle = "Airplane: Boeing 747";
        String articleContentForSecondArticle = "The Boeing 747 is a large, long-range wide-body airliner designed and manufactured by Boeing Commercial Airplanes in the United States between 1968 and 2023. After introducing the 707 in October 1958, Pan Am wanted a jet 2+1⁄2 times its size, to reduce its seat cost by 30%. In 1965, Joe Sutter left the 737 development program to design the 747. ";
        CreateArticleDto createArticleDtoForSecondArticle = new CreateArticleDto(articleNameForSecondArticle, articleContentForSecondArticle);
        articleService.addArticle(username, createArticleDtoForSecondArticle);

        String articleNameForThirdArticle = "Car: Porsche 911";
        String articleContentForThirdArticle = "Porsche 911 — общее название семейства спортивных автомобилей и автомобилей категории GT, выпускающихся компанией немецкой Porsche AG с 1965 года по настоящее время.";
        CreateArticleDto createArticleDtoForThirdArticle = new CreateArticleDto(articleNameForThirdArticle, articleContentForThirdArticle);
        articleService.addArticle(username, createArticleDtoForThirdArticle);

        SearchQueryDto searchQueryDto = new SearchQueryDto("Car",ARTICLE_FILTER_ID);
        List<ArticleDtoSearchPreview> foundArticles = (List<ArticleDtoSearchPreview>) searchService.search(searchQueryDto).getBody();
        Assertions.assertEquals(articleNameForFirstArticle, foundArticles.get(0).articleName());
        Assertions.assertEquals(articleNameForThirdArticle, foundArticles.get(1).articleName());
        user = profileServices.refreshUserData(user);
        List<Article> articlesForDelete = user.getArticles();
        for (Article article : articlesForDelete) {
            articleService.deleteArticle(user.getUsername(), article.getId().toString());
        }
        profileServices.deleteUser(user);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void whenUseSingleKeywordSearchMethodToSearchForUser_thanShouldGetExpectedUsers() {
        short USER_FILTER_ID = 1;
        String usernameFirstUser = "Boris";
        String passwordFirstUser = "12345";
        UserRegistrationRequest userRequestForFirstUser = new UserRegistrationRequest(usernameFirstUser, passwordFirstUser);
        profileServices.createUser(userRequestForFirstUser);

        String usernameSecondUser = "testUser";
        String passwordSecondUser = "12345";
        UserRegistrationRequest userRequestForSecondUser = new UserRegistrationRequest(usernameSecondUser, passwordSecondUser);
        profileServices.createUser(userRequestForSecondUser);

        String usernameThirdUser = "Boris12345";
        String passwordThirdUser = "12345";
        UserRegistrationRequest userRequestForThirdUser = new UserRegistrationRequest(usernameThirdUser, passwordThirdUser);
        profileServices.createUser(userRequestForThirdUser);

        SearchQueryDto searchQueryDto = new SearchQueryDto("boris",USER_FILTER_ID);
        List<UserDtoSearchPreview> foundUsers = (List<UserDtoSearchPreview>) searchService.search(searchQueryDto).getBody();
        Assertions.assertEquals(foundUsers.size(),2);
        Assertions.assertEquals(usernameFirstUser, foundUsers.get(0).username());
        Assertions.assertEquals(usernameThirdUser, foundUsers.get(1).username());
        profileServices.deleteUserByUsername(usernameFirstUser);
        profileServices.deleteUserByUsername(usernameSecondUser);
        profileServices.deleteUserByUsername(usernameThirdUser);
    }

    private User createEmptyTestUser() {
        String username = "testUser";
        String password = "12345";
        UserRegistrationRequest userRequest = new UserRegistrationRequest(username, password);
        return profileServices.createUser(userRequest);
    }
}
