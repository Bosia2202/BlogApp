package com.denisvasilenko.BlogApp.yandexCloudStore;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CloudYandexStore{
    private AmazonS3 s3Client;
    private String bucketName;
    public CloudYandexStore(String bucketName){
        AWSCredentials credentials = new BasicAWSCredentials(
                "YCAJExVGFzxJoYQ3X5rDuRWOX", "YCMqgcw71r3382pOr0m3wo-Y77gsdXXhZvSTdofp"
        );
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(
                        new AmazonS3ClientBuilder.EndpointConfiguration(
                                "storage.yandexcloud.net", "ru-central1"
                        )
                )
                .build();
        this.s3Client=s3Client;
        this.bucketName=bucketName;
    }
    public void cloudCreateBucket() {
        AWSCredentials credentials = new BasicAWSCredentials(
                "YCAJExVGFzxJoYQ3X5rDuRWOX", "YCMqgcw71r3382pOr0m3wo-Y77gsdXXhZvSTdofp"
        );
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(
                        new AmazonS3ClientBuilder.EndpointConfiguration(
                                "storage.yandexcloud.net", "ru-central1"
                        )
                )
                .build();
    }
    public void showAllFiles() {
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                    .withBucketName(bucketName);
            ObjectListing objectListing = s3Client.listObjects(listObjectsRequest);
            System.out.println("Object List:");
            while (true) {
                for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                    System.out.println("name=" + objectSummary.getKey() + ", size=" + objectSummary.getSize() + ", owner=" + objectSummary.getOwner().getId());
                }
                if (objectListing.isTruncated()) {
                    objectListing = s3Client.listNextBatchOfObjects(objectListing);
                } else {
                    break;
                }
            }
        } catch (AmazonS3Exception e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }

    public void uploadFiles(String articleName,String ArticleContent){
        String objectName = articleName;
        File tempFile=new File("temp.txt");
        try {
            FileWriter fileWriter= new FileWriter(tempFile);
            fileWriter.write(ArticleContent);
            fileWriter.close();
            s3Client.putObject(bucketName, objectName,tempFile);
            System.out.format("Object %s has been created.\n", objectName);
            tempFile.deleteOnExit();
            System.out.println("TempFile deleted");
            String url = s3Client.getUrl(bucketName,objectName).toString();
            System.out.println("URL: "+url);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void getArticleText(){

    }

}


