package com.denisvasilenko.blogapp.cloud;

import com.denisvasilenko.blogapp.exceptions.Cloud.CloudArticleTextDoesntCanReadRuntimeException;
import com.denisvasilenko.blogapp.exceptions.Cloud.CloudArticleTextDoesntCreatedRuntimeExceptions;
import com.denisvasilenko.blogapp.exceptions.Cloud.CloudArticleTextDoesntUpdateRuntimeException;
import com.denisvasilenko.blogapp.exceptions.UrlParser.NotValidLinkRuntimeException;
import com.denisvasilenko.blogapp.models.Article;
import com.denisvasilenko.blogapp.models.User;
import com.denisvasilenko.blogapp.yandexCloudStore.DTO.CloudUploadRequest;
import com.denisvasilenko.blogapp.yandexCloudStore.YaCloudService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class YaCloudServiceTest {

    @Autowired
    private YaCloudService yaCloudService;

    @Test
    public void whenUploadFileAndGetText_thanShouldToGetExpectedText() {
        String bucket = "blogapp";
        String key = "TestingKey";
        String articleName = "TestingArticleName";
        String expectedText = "TestArticleContent";
        User user = new User();
        LocalDate date = LocalDate.now();
        CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(bucket,key,articleName,expectedText,user,date);
        Article article = yaCloudService.uploadText(cloudUploadRequest);
        String actualText = yaCloudService.getArticleText(article.getUrl());
        Assertions.assertEquals(expectedText,actualText);
        yaCloudService.deleteArticle(article.getUrl());
    }

    @Test
    public void whenUploadFile_thanShouldToGetExpectedArticle() {
        String bucket = "blogapp";
        String key = "TestingKey";
        String articleName = "TestingArticleName";
        String articleText = "TestArticleContent";
        User user = new User();
        LocalDate date = LocalDate.now();
        Article expectedArticle = new Article();
        expectedArticle.setNameArticle(articleName);
        expectedArticle.setUserOwner(user);
        expectedArticle.setDateOfCreation(date);
        expectedArticle.setLikes(0);
        expectedArticle.setUrl("https://blogapp.storage.yandexcloud.net/TestingKey");
        CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(bucket,key,articleName,articleText,user,date);
        Article actualArticle = yaCloudService.uploadText(cloudUploadRequest);
        Assertions.assertEquals(expectedArticle,actualArticle);
        yaCloudService.deleteArticle(actualArticle.getUrl());
    }

    @Test
    public void whenUploadFileWithoutText_thanShouldToGetArticleDoesntCreatedRuntimeExceptions() {
        String bucket = "blogapp";
        String key = "TestingKey";
        String articleName = "TestingArticleName";
        User user = new User();
        LocalDate date = LocalDate.now();
        CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(bucket,key,articleName,null,user,date);
        Assertions.assertThrows(CloudArticleTextDoesntCreatedRuntimeExceptions.class,()->yaCloudService.uploadText(cloudUploadRequest));
    }

    @Test
    public void whenUploadInTheDoesntExistBucket_thanShouldToGetArticleDoesntCreatedRuntimeExceptions() {
        String bucket = "DoesntExistBucket";
        String key = "TestingKey";
        String articleName = "TestingArticleName";
        String articleText = "TestArticleContent";
        User user = new User();
        LocalDate date = LocalDate.now();
        CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(bucket,key,articleName,articleText,user,date);
        Assertions.assertThrows(CloudArticleTextDoesntCreatedRuntimeExceptions.class,()->yaCloudService.uploadText(cloudUploadRequest));
    }

    @Test
    public void whenPassInTheMethodGetArticleUrlWithDoesntExistArticle_thanGetException() {
        String urlToDoesNotExistArticle = "https://blogapp.storage.yandexcloud.net/TestingKey";
        Assertions.assertThrows(CloudArticleTextDoesntCanReadRuntimeException.class,()-> yaCloudService.getArticleText(urlToDoesNotExistArticle));
    }

    @Test
    public void whenDontPassTextInTheUpdateMethod_thanGetArticleDoesntUpdateRuntimeException() {
        String bucket = "blogapp";
        String key = "TestingKey";
        String articleName = "TestingArticleName";
        String expectedText = "TestArticleContent";
        User user = new User();
        LocalDate date = LocalDate.now();
        CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(bucket,key,articleName,expectedText,user,date);
        Article article = yaCloudService.uploadText(cloudUploadRequest);
        Assertions.assertThrows(CloudArticleTextDoesntUpdateRuntimeException.class,()->yaCloudService.updateText(article.getUrl(),null));
        yaCloudService.deleteArticle(article.getUrl());
    }

    @Test
    public void whenDontPassUrlInTheUpdateMethod_thanGetArticleDoesntUpdateRuntimeException() {
        String bucket = "blogapp";
        String key = "TestingKey";
        String articleName = "TestingArticleName";
        String text = "TestArticleContent";
        User user = new User();
        LocalDate date = LocalDate.now();
        CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(bucket,key,articleName,text,user,date);
        Article article = yaCloudService.uploadText(cloudUploadRequest);
        Assertions.assertThrows(NotValidLinkRuntimeException.class,()->yaCloudService.updateText(null,text));
        yaCloudService.deleteArticle(article.getUrl());
    }

    @Test
    public void whenUpdateArticleWithHelpMethodUpdateArticle_thanShouldGetUpdatedText() {
        String bucket = "blogapp";
        String key = "TestingKey";
        String articleName = "TestingArticleName";
        String oldText = "TestArticleContent";
        String expectedText = "This is update text";
        User user = new User();
        LocalDate date = LocalDate.now();
        CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(bucket,key,articleName,oldText,user,date);
        Article article = yaCloudService.uploadText(cloudUploadRequest);
        yaCloudService.updateText(article.getUrl(),expectedText);
        String actualText = yaCloudService.getArticleText(article.getUrl());
        Assertions.assertEquals(expectedText,actualText);
    }

    @Test
    public void whenDeleteArticle_thanShouldGetExceptionsArticleDoesntCanRead() {
        String bucket = "blogapp";
        String key = "TestingKey";
        String articleName = "TestingArticleName";
        String oldText = "TestArticleContent";
        User user = new User();
        LocalDate date = LocalDate.now();
        CloudUploadRequest cloudUploadRequest = new CloudUploadRequest(bucket,key,articleName,oldText,user,date);
        Article article = yaCloudService.uploadText(cloudUploadRequest);
        yaCloudService.deleteArticle(article.getUrl());
        Assertions.assertThrows(CloudArticleTextDoesntCanReadRuntimeException.class,()->yaCloudService.getArticleText(article.getUrl()));
    }

    @Test
    public void whenDeleteArticleWithNullUrl_thanShouldGetNotValidLinkRuntimeException() {
        Assertions.assertThrows(NotValidLinkRuntimeException.class,()->yaCloudService.deleteArticle(null));
    }
}
