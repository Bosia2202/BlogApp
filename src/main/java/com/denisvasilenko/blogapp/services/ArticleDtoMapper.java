package com.denisvasilenko.blogapp.services;

import com.denisvasilenko.blogapp.DTO.ArticleDto.ArticleDto;
import com.denisvasilenko.blogapp.models.Article;
import com.denisvasilenko.blogapp.yandexCloudStore.YaCloudService;

public class ArticleDtoMapper {

    public ArticleDto getTextFromYandexCloud(Article article){
        YaCloudService yaCloudService = new YaCloudService();
       String articleText = yaCloudService.getArticleText(article.getUrl());

        return new ArticleDto (
                article.getNameArticle(),
                articleText,
                article.getLikes(),
                article.getDateOfCreation(),
                article.getUserOwner().getUsername());
    }
}
