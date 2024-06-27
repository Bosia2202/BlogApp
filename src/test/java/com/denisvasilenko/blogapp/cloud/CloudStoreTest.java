package com.denisvasilenko.blogapp.cloud;

import com.denisvasilenko.blogapp.cloudStore.CloudStoreInterface;
import com.denisvasilenko.blogapp.exceptions.cloud.CloudArticleTextDoesntCanReadRuntimeException;
import com.denisvasilenko.blogapp.exceptions.cloud.CloudArticleTextDoesntCreatedRuntimeExceptions;
import com.denisvasilenko.blogapp.exceptions.cloud.CloudArticleTextDoesntUpdateRuntimeException;
import com.denisvasilenko.blogapp.exceptions.urlParser.NotValidLinkRuntimeException;
import com.denisvasilenko.blogapp.models.Article;
import com.denisvasilenko.blogapp.models.User;
import com.denisvasilenko.blogapp.cloudStore.DTO.CloudUploadRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalDate;

@PropertySource("classpath:application-test.properties")
@SpringBootTest
public class CloudStoreTest {

    private final CloudStoreInterface cloudStore;

    @Value("${S3CloudStore.bucket}")
    private String bucket;

    @Autowired
    public CloudStoreTest(CloudStoreInterface cloudStore) {
        this.cloudStore = cloudStore;
    }

    @Test
    public void whenUploadFileAndGetText_thanShouldToGetExpectedText() {
        String articleName = "TestingArticleName";
        String expectedText = "TestArticleContent";
        User user = new User();
        LocalDate date = LocalDate.now();
        CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(bucket, articleName, expectedText, user, date);
        Article article = cloudStore.uploadText(cloudUploadRequest);
        String actualText = cloudStore.getArticleTextByUrl(article.getUrl());
        Assertions.assertEquals(expectedText, actualText);
        cloudStore.deleteArticle(article.getUrl());
    }

    @Test
    public void whenUploadFile_thanShouldToGetExpectedArticle() {
        String articleName = "TestingArticleName";
        String articleText = "TestArticleContent";
        User user = new User();
        LocalDate date = LocalDate.now();
        Article expectedArticle = new Article();
        expectedArticle.setNameArticle(articleName);
        expectedArticle.setUserOwner(user);
        expectedArticle.setDateOfCreation(date);
        expectedArticle.setLikes(0);
        CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(bucket, articleName, articleText, user, date);
        expectedArticle.setId(cloudUploadRequest.getArticleId());
        expectedArticle.setUrl("https://blogapp.storage.yandexcloud.net/" + cloudUploadRequest.getArticleId());
        Article actualArticle = cloudStore.uploadText(cloudUploadRequest);
        Assertions.assertEquals(expectedArticle, actualArticle);
        cloudStore.deleteArticle(actualArticle.getUrl());
    }

    @Test
    public void whenUploadFileWithoutText_thanShouldToGetArticleDoesNotCreatedRuntimeExceptions() {
        String articleName = "TestingArticleName";
        User user = new User();
        LocalDate date = LocalDate.now();
        CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(bucket, articleName, null, user, date);
        Assertions.assertThrows(CloudArticleTextDoesntCreatedRuntimeExceptions.class, () -> cloudStore.uploadText(cloudUploadRequest));
    }

    @Test
    public void whenUploadInTheDoesNotExistBucket_thanShouldToGetArticleDoesntCreatedRuntimeExceptions() {
        String bucket = "DoesNotExistBucket";
        String articleName = "TestingArticleName";
        String articleText = "TestArticleContent";
        User user = new User();
        LocalDate date = LocalDate.now();
        CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(bucket, articleName, articleText, user, date);
        Assertions.assertThrows(CloudArticleTextDoesntCreatedRuntimeExceptions.class, () -> cloudStore.uploadText(cloudUploadRequest));
    }

    @Test
    public void whenPassInTheMethodGetArticleUrlWithDoesNotExistArticle_thanGetException() {
        String urlToDoesNotExistArticle = "https://blogapp.storage.yandexcloud.net/TestingKey";
        Assertions.assertThrows(CloudArticleTextDoesntCanReadRuntimeException.class, () -> cloudStore.getArticleTextByUrl(urlToDoesNotExistArticle));
    }

    @Test
    public void whenDoNotPassTextInTheUpdateMethod_thanGetArticleDoesntUpdateRuntimeException() {
        String articleName = "TestingArticleName";
        String expectedText = "TestArticleContent";
        User user = new User();
        LocalDate date = LocalDate.now();
        CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(bucket, articleName, expectedText, user, date);
        Article article = cloudStore.uploadText(cloudUploadRequest);
        Assertions.assertThrows(CloudArticleTextDoesntUpdateRuntimeException.class, () -> cloudStore.updateText(article.getUrl(), null));
        cloudStore.deleteArticle(article.getUrl());
    }

    @Test
    public void whenDoNotPassUrlInTheUpdateMethod_thanGetArticleDoesntUpdateRuntimeException() {
        String articleName = "TestingArticleName";
        String text = "TestArticleContent";
        User user = new User();
        LocalDate date = LocalDate.now();
        CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(bucket, articleName, text, user, date);
        Article article = cloudStore.uploadText(cloudUploadRequest);
        Assertions.assertThrows(NotValidLinkRuntimeException.class, () -> cloudStore.updateText(null, text));
        cloudStore.deleteArticle(article.getUrl());
    }

    @Test
    public void whenUpdateArticleWithHelpMethodUpdateArticle_thanShouldGetUpdatedText() {
        String articleName = "TestingArticleName";
        String oldText = "TestArticleContent";
        String expectedText = "This is update text";
        User user = new User();
        LocalDate date = LocalDate.now();
        CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(bucket, articleName, oldText, user, date);
        Article article = cloudStore.uploadText(cloudUploadRequest);
        cloudStore.updateText(article.getUrl(), expectedText);
        String actualText = cloudStore.getArticleTextByUrl(article.getUrl());
        Assertions.assertEquals(expectedText, actualText);
        cloudStore.deleteArticle(article.getUrl());
    }

    @Test
    public void whenDeleteArticle_thanShouldGetExceptionsArticleDoesNotCanRead() {
        String articleName = "TestingArticleName";
        String oldText = "TestArticleContent";
        User user = new User();
        LocalDate date = LocalDate.now();
        CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(bucket, articleName, oldText, user, date);
        Article article = cloudStore.uploadText(cloudUploadRequest);
        cloudStore.deleteArticle(article.getUrl());
        Assertions.assertThrows(CloudArticleTextDoesntCanReadRuntimeException.class, () -> cloudStore.getArticleTextByUrl(article.getUrl()));
    }

    @Test
    public void whenDeleteArticleWithNullUrl_thanShouldGetNotValidLinkRuntimeException() {
        Assertions.assertThrows(NotValidLinkRuntimeException.class, () -> cloudStore.deleteArticle(null));
    }
}
