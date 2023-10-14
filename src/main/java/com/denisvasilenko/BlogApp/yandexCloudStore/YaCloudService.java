package com.denisvasilenko.BlogApp.yandexCloudStore;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.model.Put;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;


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
        File tempFile=new File("temp.txt");
        try {
            FileWriter fileWriter= new FileWriter(tempFile);
            fileWriter.write(ArticleContent);
            fileWriter.close();
            log.debug("Temp text file created and wrote article content text ");
            s3Client.putObject(bucketName,Author + key,tempFile);
            log.debug("Object "+key+ " has been created.");
            tempFile.deleteOnExit();
            log.debug("Temp text file delete");
        } catch (AmazonS3Exception e) {
            log.error("Failed loading file" +
                    "Error AmazonS3Exception "+e);
        } catch(SdkClientException e) {
            log.error("Failed loading file" +
                    "Error SdkClientException "+e);
        } catch (IOException e) {
            log.error("Failed to writing file");
        }
    }
    public String getArticleText(String key){
        try {
            S3Object s3Object = s3Client.getObject(bucketName, key);
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
    private boolean isFolderExists(String key) {
        ListObjectsV2Result result = this.s3Client.listObjectsV2(this.bucketName, key);
        return result.getKeyCount() > 0;
    }
}


