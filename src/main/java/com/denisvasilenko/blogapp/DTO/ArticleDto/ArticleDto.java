package com.denisvasilenko.blogapp.DTO.ArticleDto;

import java.util.Date;

public record ArticleDto(
        String nameArticle,
        String text,
        int likes,
        Date dateOfCreation,
        String Author) {
}
