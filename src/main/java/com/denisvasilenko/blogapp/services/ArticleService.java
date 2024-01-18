package com.denisvasilenko.blogapp.services;

import com.denisvasilenko.blogapp.DTO.ArticleDto.ArticleDto;
import com.denisvasilenko.blogapp.DTO.ArticleDto.CreateArticleDto;
import com.denisvasilenko.blogapp.DTO.ArticleDto.UpdateArticleDto;
import com.denisvasilenko.blogapp.exceptions.AccessException;
import com.denisvasilenko.blogapp.exceptions.articleException.NotFoundArticleException;
import com.denisvasilenko.blogapp.models.Article;
import com.denisvasilenko.blogapp.models.User;
import com.denisvasilenko.blogapp.repositories.ArticleRepository;
import com.denisvasilenko.blogapp.yandexCloudStore.DTO.CloudUploadRequest;
import com.denisvasilenko.blogapp.yandexCloudStore.YaCloudService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

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

    public ArticleDto showArticle(String articleId) {
        UUID articleUUID = UUID.fromString(articleId);
        Article article = processingTheArticleFound(articleUUID,articleRepository::findById);
           return new ArticleDto (
                   article.getNameArticle(),
                   getArticleText(article),
                   article.getLikes(),
                   article.getDateOfCreation(),
                   article.getUserOwner().getUsername()
        );
    }

    public Article findArticleById (UUID articleId) {
        return processingTheArticleFound(articleId,articleRepository::findById);
    }

    public Article findByArticleName(String articleName) {
        return processingTheArticleFound(articleName,articleRepository::findByNameArticle);
    }

    public List<Article> findAllArticlesByArticleName(String articleName) {
        return articleRepository.findAllByNameArticleContainingIgnoreCaseOrderByDateOfCreation(articleName);
    }

    @Transactional
    public List<Article> searchArticlesByNameArticleFromSpecificUser(String articleName, String username) {
        User specificUser = profileServices.findUserByUserName(username);
        return specificUser.getArticles().stream().filter(article -> article.getNameArticle().toLowerCase()
                .contains(articleName.toLowerCase()))
                .toList();
    }

    @Transactional
    public ResponseEntity<String> addArticle(@NotNull String userName,@NotNull CreateArticleDto createArticleDto) {
         User userOwner = profileServices.findUserByUserName(userName);
         CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(
                 "blogapp",
                 createArticleDto.articleName(),
                 createArticleDto.articleContent(),
                 userOwner,
                 LocalDate.now()
         );
         Article article = yaCloudService.uploadText(cloudUploadRequest);
         articleRepository.save(article);
         log.info("Article " + createArticleDto.articleName() + " by " + userName + " added in database");
         return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> updateArticle(@NotNull String currentUser,String articleId ,@NotNull UpdateArticleDto updateArticleDto) {
           Article article = checkingAccessRightsOfTheCurrentUserToModifyArticles(currentUser,UUID.fromString(articleId));
           if (updateArticleDto.newArticleName() != null) {
               article.setNameArticle(updateArticleDto.newArticleName());
               articleRepository.save(article);
               log.info("Article '" + article.getNameArticle() + "' successfully change to '" + updateArticleDto.newArticleName() + "'");
           }
           if(updateArticleDto.newArticleContent() != null) {
               yaCloudService.updateText(article.getUrl(), updateArticleDto.newArticleContent());
               log.info("Text successfully change");
           }
           log.info("Article " + article.getNameArticle() + "' successfully update");
           return new ResponseEntity<>(HttpStatus.OK);
   }

    @Transactional
    public ResponseEntity<String> deleteArticle(@NotNull String userNameByCurrentUser, @NotNull String articleId) {
        Article article = checkingAccessRightsOfTheCurrentUserToModifyArticles(userNameByCurrentUser,UUID.fromString(articleId));
        yaCloudService.deleteArticle(article.getUrl());
        articleRepository.deleteById(article.getId());
        log.info("ARTICLE : " + article.getId() + " deleted");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Article checkingAccessRightsOfTheCurrentUserToModifyArticles(@NotNull String userNameByCurrentUser, @NotNull UUID checkingArticleId) {
        User currentUser = profileServices.findUserByUserName(userNameByCurrentUser);
        if(currentUser.getArticles().stream().noneMatch(article -> article.getId().equals(checkingArticleId))){
            throw new AccessException();
        }
        log.info("User can delete article! Checking access is access!");
        return processingTheArticleFound(checkingArticleId,articleRepository::findById);
    }

    private Article processingTheArticleFound(UUID searchParam, Function<UUID,Optional<Article>> finder) {
        return finder.apply(searchParam).orElseThrow(() -> new NotFoundArticleException(searchParam));
    }

    private Article processingTheArticleFound(String searchParam, Function<String,Optional<Article>> finder) {
        return finder.apply(searchParam).orElseThrow(() -> new NotFoundArticleException(searchParam));
    }

    private String getArticleText(Article article) {
        return yaCloudService.getArticleTextByUrl(article.getUrl());
    }
}
