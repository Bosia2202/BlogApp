package com.denisvasilenko.BlogApp.services;

import com.denisvasilenko.BlogApp.DTO.ArticleDto.ArticleDto;
import com.denisvasilenko.BlogApp.models.Article;
import com.denisvasilenko.BlogApp.yandexCloudStore.YaCloudService;

public class TextGetter {
    public ArticleDto getTextFromYandexCloud(Article article){
        YaCloudService yaCloudService=new YaCloudService();
        return new ArticleDto(
                article.getNameArticle(),
                yaCloudService.getArticleText(article.getUrl()),
                article.getLikes(),
                article.getDateOfCreation());
    }
}
