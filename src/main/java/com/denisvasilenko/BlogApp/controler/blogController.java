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
        yaCloudService.parseURL("https://storage.yandexcloud.net/blogapp/test_file_1?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=YCAJEBQcUDPOO9hHVzSfsFHLv%2F20231017%2Fru-central1%2Fs3%2Faws4_request&X-Amz-Date=20231017T102227Z&X-Amz-Expires=3600&X-Amz-Signature=F1D1F8B2F110CCCC331EE1F980C0DD3AB789495171BD0355DA37FC351DF3C521&X-Amz-SignedHeaders=host");
        //yaCloudService.uploadFiles("sas","Mama","Всем привет новый метод записи");
//        cloudYandexStore.uploadFiles("How to Create bucket","Hello, today i show you how to create bucket in Yandex.Store Object!" +
//                "it is a beautiful service");
        yaCloudService.getArticleText("How to Create bucket");
        return "Method end";
    }
}
