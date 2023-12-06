package com.denisvasilenko.BlogApp.services;

import com.denisvasilenko.BlogApp.DTO.ArticleDto.ArticleDto;
import com.denisvasilenko.BlogApp.DTO.ArticleDto.CreateArticleDto;
import com.denisvasilenko.BlogApp.DTO.ArticleDto.UpdateArticleDto;
import com.denisvasilenko.BlogApp.exceptions.AccessException;
import com.denisvasilenko.BlogApp.exceptions.Cloud.ArticleDoesntCreatedRuntimeExceptions;
import com.denisvasilenko.BlogApp.exceptions.Cloud.ArticleDoesntUpdateRuntimeException;
import com.denisvasilenko.BlogApp.exceptions.NotFoundArticleException;
import com.denisvasilenko.BlogApp.exceptions.NotFoundUserException;
import com.denisvasilenko.BlogApp.models.Article;
import com.denisvasilenko.BlogApp.models.User;
import com.denisvasilenko.BlogApp.repositories.ArticleRepository;
import com.denisvasilenko.BlogApp.yandexCloudStore.DTO.CloudUploadRequest;
import com.denisvasilenko.BlogApp.yandexCloudStore.YaCloudService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ProfileServices profileServices;
    private final YaCloudService yaCloudService;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, ProfileServices profileServices, YaCloudService yaCloudService) {
        this.articleRepository = articleRepository;
        this.profileServices = profileServices;
        this.yaCloudService = yaCloudService;
    }

    public ArticleDto showArticle(String username, String articleName) {
            Article article = checkArticleByUser(username, articleName).orElseThrow(()->new NotFoundArticleException(articleName));
            ArticleDtoMapper articleDtoMapper = new ArticleDtoMapper();
            return articleDtoMapper.getTextFromYandexCloud(article);
    }

    @Transactional
    public ResponseEntity<String> addArticle(@NotNull String username,@NotNull CreateArticleDto createArticleDto) {
         User userOwner = profileServices.findUserByUserName(username)
                 .orElseThrow(() -> new NotFoundUserException(username));
         String userName = userOwner.getUsername();
         UUID uuid = UUID.randomUUID();
         String articleIdentifier = uuid + userName + createArticleDto.articleName();
         CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(
                 "blogapp",
                 articleIdentifier,
                 createArticleDto.articleName(),
                 createArticleDto.articleContent(),
                 userOwner,
                 getCurrentDate()
         );
         Article article = yaCloudService.uploadText(cloudUploadRequest).orElseThrow(() -> new ArticleDoesntCreatedRuntimeExceptions(createArticleDto.articleName()));
         articleRepository.save(article);
         log.info("Article " + createArticleDto + " by " + userName + " added in database");
         return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> updateArticle(@NotNull String articleAuthor,@NotNull String requestingUser, String articleName,@NotNull UpdateArticleDto updateArticleDto) {
           сheckAccessRightsOfTheRequestingUser(requestingUser,articleAuthor);
           Article article = checkArticleByUser(articleAuthor, articleName).orElseThrow(()->new NotFoundArticleException(articleName));
           if (!updateArticleDto.newArticleName().equals(articleName)) {
               article.setNameArticle(updateArticleDto.newArticleName());
               articleRepository.save(article);
               log.info("Article '" + articleName + "' successfully change to '" + updateArticleDto.newArticleName() + "'");
           }
           yaCloudService.updateText(article.getUrl(), updateArticleDto.newArticleContent());
           log.info("Article "+articleName+"' successfully update");
           return new ResponseEntity<>(HttpStatus.OK);
   }

    @Transactional
    public ResponseEntity<String> deleteArticle(@NotNull String articleAuthor, @NotNull String requestingUser, @NotNull String articleName) {
        сheckAccessRightsOfTheRequestingUser(requestingUser,articleAuthor);
        Article article = checkArticleByUser(articleAuthor,articleName)
                .orElseThrow(() -> new NotFoundArticleException(articleName));
        yaCloudService.deleteArticle(article.getUrl());
        log.info("ARTICLE : " + article.getId());
        articleRepository.deleteById(article.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Optional<Article> checkArticleByUser(String username, String articleName) {
        User user = profileServices.findUserByUserName(username)
                .orElseThrow(() -> new NotFoundUserException(username));
        List<Article> articlesByUser = user.getArticles();
        return articlesByUser.stream().filter(article -> article.getNameArticle().equals(articleName)).findFirst();
    }

    private void сheckAccessRightsOfTheRequestingUser(@NotNull String requestingUser, @NotNull String articleAuthor) {
        if (!requestingUser.equals(articleAuthor)) {
            throw new AccessException();
        }
    }

    private Date getCurrentDate() {
        long mills = System.currentTimeMillis();
        return new Date(mills);
    }
}
