package com.denisvasilenko.blogapp.DTO.ArticleDto;

import java.time.LocalDate;

public record ArticleDto(
        String nameArticle,
        String text,
        int likes,
        LocalDate dateOfCreation,
        String Author) {
}
