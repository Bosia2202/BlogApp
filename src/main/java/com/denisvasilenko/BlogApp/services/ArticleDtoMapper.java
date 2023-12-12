package com.denisvasilenko.BlogApp.services;

import com.denisvasilenko.BlogApp.DTO.ArticleDto.ArticleDto;
import com.denisvasilenko.BlogApp.models.Article;
import com.denisvasilenko.BlogApp.yandexCloudStore.YaCloudService;

import java.util.Optional;

public class ArticleDtoMapper {

    public  ArticleDto getTextFromYandexCloud(Article article){
        YaCloudService yaCloudService = new YaCloudService();
       String articleText = yaCloudService.getArticleText(article.getUrl());

        return new ArticleDto(
                article.getNameArticle(),
                articleText,
                article.getLikes(),
                article.getDateOfCreation(),
                article.getUserOwner().getUsername());
    }
}
