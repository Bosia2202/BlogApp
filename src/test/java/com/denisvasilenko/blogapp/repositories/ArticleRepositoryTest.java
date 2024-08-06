package com.denisvasilenko.blogapp.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
public class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void deleteArticleTest() {
        String UUIDFromDataBase = "e1ca68f2-cba5-4e25-afef-b6e38fd28e71";
        UUID testUUID = UUID.fromString(UUIDFromDataBase);
        articleRepository.deleteById(testUUID);
    }
}
