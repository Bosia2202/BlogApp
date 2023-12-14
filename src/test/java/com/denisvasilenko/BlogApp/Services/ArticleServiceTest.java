package com.denisvasilenko.BlogApp.Services;

import com.denisvasilenko.BlogApp.models.User;
import com.denisvasilenko.BlogApp.services.ArticleService;
import com.denisvasilenko.BlogApp.yandexCloudStore.DTO.CloudUploadRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ArticleServiceTest {

    @Autowired
    ArticleService articleService;

    @Mock
    User user;

//    CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(
//            "blogapp",
//            "articleIdentifier",
//            "createArticleDto.articleName",
//            "createArticleDto.articleContent()",
//            userOwner,
//            getCurrentDate()
//    );
}
