package com.denisvasilenko.BlogApp.DTO.ArticleDto;

import com.denisvasilenko.BlogApp.models.User;

import java.util.Date;

public record ArticleDto(
        String nameArticle,
        String text,
        int likes,
        Date dateOfCreation,
        String Author) {

}
