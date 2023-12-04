package com.denisvasilenko.BlogApp.services;

import com.denisvasilenko.BlogApp.DTO.ArticleDto.ArticleDto;
import com.denisvasilenko.BlogApp.models.Article;
import com.denisvasilenko.BlogApp.yandexCloudStore.YaCloudService;

import java.util.Optional;

public class ArticleDtoMapper {

    public  ArticleDto getTextFromYandexCloud(Article article){
        Optional<String> text = getTextFromYandexCloud2(article.getUrl());
        if(text.isEmpty()){
            return null;
        }
        return new ArticleDto(
                article.getNameArticle(),
                text.get(),
                article.getLikes(),
                article.getDateOfCreation(),
                article.getUserOwner().getUsername());
    }
    private Optional<String> getTextFromYandexCloud2(String url) {
        YaCloudService yaCloudService = new YaCloudService();
        return yaCloudService.getArticleText(url);
    }
}
