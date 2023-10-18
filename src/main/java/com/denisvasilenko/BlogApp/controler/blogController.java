package com.denisvasilenko.BlogApp.controler;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3EncryptionClientV2Builder;
import com.denisvasilenko.BlogApp.yandexCloudStore.YaCloudService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.net.URISyntaxException;

@Controller
public class blogController {
    @GetMapping("/test")
    public String TestContoller() throws IOException, URISyntaxException {
        YaCloudService yaCloudService =new YaCloudService();
        yaCloudService.getAllArticles("blogapp");
        //yaCloudService.uploadFiles("sas","Mama","Всем привет новый метод записи");
//        cloudYandexStore.uploadFiles("How to Create bucket","Hello, today i show you how to create bucket in Yandex.Store Object!" +
//                "it is a beautiful service");
     //   yaCloudService.getArticleText("How to Create bucket");
        return "Method end";
    }
}
