package com.denisvasilenko.blogapp.DTO.ArticleDto;

import java.util.UUID;

public record UpdateArticleDto(UUID articleId, String newArticleName, String newArticleContent) {
}
