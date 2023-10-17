package com.denisvasilenko.BlogApp.yandexCloudStore;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.model.Put;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Service
@Log4j2
public class YaCloudService{

    private final AmazonS3 s3Client;
    private final String bucketName="blogapp";
    public YaCloudService(){
        AWSCredentials credentials = new BasicAWSCredentials(
                "YCAJExVGFzxJoYQ3X5rDuRWOX", "YCMqgcw71r3382pOr0m3wo-Y77gsdXXhZvSTdofp"
        );
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(
                        new AmazonS3ClientBuilder.EndpointConfiguration(
                                "storage.yandexcloud.net", "ru-central1"
                        )
                )
                .build();
        log.debug("S3Client created and connected to YandexObject");
    }

    public void uploadFiles(String Author,String key,String ArticleContent){
        byte[] contentBytes = ArticleContent.getBytes();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentBytes.length);
        PutObjectRequest request = new PutObjectRequest(bucketName, "test_file_1", new ByteArrayInputStream(contentBytes), metadata);
        s3Client.putObject(request);
        System.out.println("Текст успешно загружен в бакет.");
    }
    public String getArticleText(String key){
        try {
            S3Object s3Object = s3Client.getObject(bucketName, key);
            String url=s3Client.getUrl(bucketName,key).toString();
            System.out.println(url);
            log.debug("Get file");
            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
            String ObjContent = IOUtils.toString(s3ObjectInputStream);
            log.debug("Article content got and return");
            return ObjContent;
        }
        catch (IOException e){
            log.error("Failed to convert to String" +e);
            return null;
        }
    }
    public void parseURL(String StringURL){
        URL url = null;
        try {
            url = new URL(StringURL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        URI uri = null;
        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        System.out.println(uri);
            String d = uri.getQuery();
            System.out.println(d);
    }
}

