package com.denisvasilenko.blogapp.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
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
