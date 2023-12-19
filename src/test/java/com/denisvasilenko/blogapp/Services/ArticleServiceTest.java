package com.denisvasilenko.blogapp.Services;

import com.denisvasilenko.blogapp.models.User;
import com.denisvasilenko.blogapp.repositories.ArticleRepository;
import com.denisvasilenko.blogapp.services.ArticleService;
import com.denisvasilenko.blogapp.services.ProfileServices;
import com.denisvasilenko.blogapp.yandexCloudStore.YaCloudService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ArticleServiceTest {

    @Autowired
    ArticleService articleService;

    @Mock
    private User user;

    @Mock
    private ProfileServices profileServices;

    @Mock
    private YaCloudService yaCloudService;

    @Mock
    private ArticleRepository articleRepository;
    @Test
    public void testAddMethod() {
        when(user.getUsername()).thenReturn("TestUser");
    }
}
