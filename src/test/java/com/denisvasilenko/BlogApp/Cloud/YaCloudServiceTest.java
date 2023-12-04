package com.denisvasilenko.BlogApp.Cloud;

import com.denisvasilenko.BlogApp.yandexCloudStore.YaCloudService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class YaCloudServiceTest {

    @Autowired
    private YaCloudService yaCloudService;

    @Test
    public void whenWeUploadFile_thanWeShouldToGetExpectedText() {
        String bucketName = "blogapp";
        String key = "test_article_for_whenWeUploadFile_thanWeShouldToGetText_TEST";
        String expectArticleContent = "This is test text for whenWeUploadFile_thanWeShouldToGetText_TEST";
        String articleULR = yaCloudService.uploadFiles(bucketName,key,expectArticleContent).get();
        String actualArticleContent = yaCloudService.getArticleText(articleULR);
        Assertions.assertEquals(expectArticleContent,actualArticleContent);
        yaCloudService.deleteArticle(articleULR);
    }

    @Test
    public void whenWeUploadFileWithWrongKey_thanWeGetNullUrl() {
        String wrongBucketName = "TEST_WRONG_BUCKET_NAME";
        String key = "test_article_for_whenWeUploadFile_thanWeShouldToGetText_TEST";
        String articleContent = "This is test text for whenWeUploadFile_thanWeShouldToGetText_TEST";
        String articleULR = yaCloudService.uploadFiles(wrongBucketName,key,articleContent).orElse(null);
        Assertions.assertNull(articleULR);
    }

    @Test
    public void whenWeUploadFileWithoutText_thanWeDoesntShouldToGetText() {
        String bucketName = "blogapp";
        String key = "test_article_for_whenWeUploadFileWithoutText_thanWeDoesntShouldToGetText_TEST";
        String articleULR = yaCloudService.uploadFiles(bucketName,key,null).orElse(null);
        Assertions.assertNull(articleULR);
        };
}
