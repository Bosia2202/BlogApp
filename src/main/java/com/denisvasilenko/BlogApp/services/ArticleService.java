package com.denisvasilenko.BlogApp.services;

import com.denisvasilenko.BlogApp.DTO.ArticleDto.ArticleDto;
import com.denisvasilenko.BlogApp.DTO.ArticleDto.CreateArticleDto;
import com.denisvasilenko.BlogApp.exceptions.AccessException;
import com.denisvasilenko.BlogApp.exceptions.NotFoundArticleException;
import com.denisvasilenko.BlogApp.exceptions.NotFoundUserException;
import com.denisvasilenko.BlogApp.models.Article;
import com.denisvasilenko.BlogApp.models.User;
import com.denisvasilenko.BlogApp.repositories.ArticleRepository;
import com.denisvasilenko.BlogApp.yandexCloudStore.UrlParser;
import com.denisvasilenko.BlogApp.yandexCloudStore.YaCloudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        if (checkArticleByUser(username, articleName)) {
            Article article = articleRepository.findByNameArticle(articleName).get();
            ArticleDtoMapper articleDtoMapper = new ArticleDtoMapper();
            return articleDtoMapper.getTextFromYandexCloud(article);
        } else throw new NotFoundArticleException(articleName);
    }

    private boolean checkArticleByUser(String username, String articleName) {
        User user = profileServices.findUserByUserName(username)
                .orElseThrow(() -> new NotFoundUserException(username));
        List<Article> articlesByUser = user.getArticles();
        boolean articleFound = articlesByUser.stream()
                .anyMatch(article -> article.getNameArticle().equals(articleName));
        if (articleFound) {
            log.info("Article " + "'" + articleName + "'" + " was found");
            return true;
        } else {
            log.info("Article " + "'" + articleName + "'" + " doesn't found");
            return false;
        }
    }

    public ResponseEntity<String> addArticle(String username, CreateArticleDto createArticleDto) {
        User user = profileServices.findUserByUserName(username)
                .orElseThrow(() -> new NotFoundUserException(username));
        String userName = user.getUsername();
        UUID uuid = UUID.randomUUID();
        String articleIdentifier = uuid + userName + createArticleDto.articleName();
        String urlByArticle = yaCloudService.uploadFiles("blogapp", articleIdentifier, createArticleDto.text());
        Article article = new Article();
        article.setNameArticle(createArticleDto.articleName());
        article.setUserOwner(user);
        article.setUrl(urlByArticle);
        article.setDateOfCreation(getCurrentDate());
        article.setLikes(0);
        articleRepository.save(article);
        log.info("Article " + createArticleDto + " by " + userName + " added in database");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private Date getCurrentDate() {
        long mills = System.currentTimeMillis();
        return new Date(mills);
    }

    public ResponseEntity<String> updateArticle(String author, String currentUser, String articleName, String newText) {
        if (author.equals(currentUser)) {
            if (checkArticleByUser(author, articleName)) {
                Article article = articleRepository.findByNameArticle(articleName).get();
                String url = article.getUrl();
                UrlParser urlParser = new UrlParser(url);
                String bucket = urlParser.getBucket();
                String key = urlParser.getKey();
                yaCloudService.uploadFiles(bucket, key, newText);
                return new ResponseEntity<>(HttpStatus.OK);
            } else
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else throw new AccessException();
    }

    public ResponseEntity<String> deleteArticle(String author, String currentUser, String articleName){
        if (author.equals(currentUser)) {
            if(checkArticleByUser(currentUser,articleName)) {
                Article article=articleRepository.findByNameArticle(articleName)
                        .orElseThrow(()->new NotFoundArticleException(articleName));
                yaCloudService.deleteArticle(article.getUrl());
                articleRepository.deleteById(article.getId());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else throw new NotFoundArticleException(articleName);
        }
        else throw new AccessException();
    }



}
