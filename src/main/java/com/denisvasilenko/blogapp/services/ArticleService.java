package com.denisvasilenko.blogapp.services;

import com.denisvasilenko.blogapp.DTO.ArticleDto.ArticleDto;
import com.denisvasilenko.blogapp.DTO.ArticleDto.CreateArticleDto;
import com.denisvasilenko.blogapp.DTO.ArticleDto.UpdateArticleDto;
import com.denisvasilenko.blogapp.exceptions.AccessException;
import com.denisvasilenko.blogapp.exceptions.NotFoundArticleException;
import com.denisvasilenko.blogapp.exceptions.userException.NotFoundUserException;
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

    public ArticleDto showArticle(Article article) {
            ArticleDtoMapper articleDtoMapper = new ArticleDtoMapper();
            return articleDtoMapper.getTextFromYandexCloud(article);
    }

    public Article findArticleById (UUID articleId) {
       return articleRepository.findById(articleId).orElseThrow(() -> new NotFoundArticleException(articleId));
    }

    public Article findByArticleName(String articleName) {
       return articleRepository.findByNameArticle(articleName).orElseThrow(() -> new NotFoundArticleException(articleName));
    }

    public List<Article> findAllArticlesByArticleName(String articleName) {
        return articleRepository.findAllByNameArticle(articleName);
    }

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
    public ResponseEntity<String> updateArticle(@NotNull String articleAuthor, @NotNull String currentUser, UUID articleId,@NotNull UpdateArticleDto updateArticleDto) {
           Article article = checkingAccessRightsOfTheCurrentUserToModifyArticles(currentUser,articleId);
           if (!updateArticleDto.newArticleName().equals(article.getNameArticle())) {
               article.setNameArticle(updateArticleDto.newArticleName());
               articleRepository.save(article);
               log.info("Article '" + article.getNameArticle() + "' successfully change to '" + updateArticleDto.newArticleName() + "'");
           }
           yaCloudService.updateText(article.getUrl(), updateArticleDto.newArticleContent());
           log.info("Article " + article.getNameArticle() + "' successfully update");
           return new ResponseEntity<>(HttpStatus.OK);
   }

    @Transactional
    public ResponseEntity<String> deleteArticle(@NotNull String userNameByCurrentUser, @NotNull UUID articleId) {
        Article article = checkingAccessRightsOfTheCurrentUserToModifyArticles(userNameByCurrentUser,articleId);
        yaCloudService.deleteArticle(article.getUrl());
        articleRepository.deleteById(article.getId());
        log.info("ARTICLE : " + article.getId() + " deleted");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Article checkingAccessRightsOfTheCurrentUserToModifyArticles(@NotNull String userNameByCurrentUser, @NotNull UUID articleId) {
        User currentUser = profileServices.findUserByUserName(userNameByCurrentUser);
        Article verifiableArticle = findArticleById(articleId);
        if(!currentUser.getArticles().stream().anyMatch(article -> article.equals(verifiableArticle))){
            throw new AccessException();
        }
        log.info("User can delete article! Checking access is access!");
        return verifiableArticle;
    }

}
