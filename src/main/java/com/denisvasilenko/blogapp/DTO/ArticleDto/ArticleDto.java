package com.denisvasilenko.blogapp.dto.articledto;

import java.time.LocalDate;

public record ArticleDto(String nameArticle, String text, int likes, LocalDate dateOfCreation, String Author) {
}
