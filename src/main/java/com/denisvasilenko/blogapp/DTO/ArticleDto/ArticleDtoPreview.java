package com.denisvasilenko.blogapp.dto.articledto;

import java.time.LocalDate;
import java.util.UUID;

public record ArticleDtoPreview(UUID id, String nameArticle, LocalDate dateOfCreation, int likes) {
}
