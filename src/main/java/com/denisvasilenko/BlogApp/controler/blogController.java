package com.denisvasilenko.BlogApp.controler;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3EncryptionClientV2Builder;
import com.denisvasilenko.BlogApp.yandexCloudStore.CloudYandexStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class blogController {
    @GetMapping("/test")
    public String TestContoller(){
        CloudYandexStore cloudYandexStore=new CloudYandexStore("blogapp");
        cloudYandexStore.uploadFiles("How to Create bucket","Hello, today i show you how to create bucket in Yandex.Store Object!" +
                "it is a beautiful service");
        return "Method end";
    }
}
