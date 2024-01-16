package com.denisvasilenko.blogapp.DTO.ArticleDto;

import java.util.UUID;

public record UpdateArticleDto(String newArticleName, String newArticleContent) {
}
