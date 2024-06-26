package com.denisvasilenko.blogapp.cloudStore;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.denisvasilenko.blogapp.exceptions.cloud.*;
import com.denisvasilenko.blogapp.models.Article;
import com.denisvasilenko.blogapp.cloudStore.DTO.CloudUploadRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
@Log4j2
public class YaCloudService implements CloudStoreInterface {
    private static final AmazonS3 s3Client;

    static {
        Properties properties = new Properties();
        try {
            properties.load(YaCloudService.class.getClassLoader().getResourceAsStream("configurations/S3CloudStore.properties"));
            AWSCredentials credentials = new BasicAWSCredentials(
                    properties.getProperty("S3CloudStore.accessKey"), properties.getProperty("S3CloudStore.secretKey"));
            s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new AmazonS3ClientBuilder.EndpointConfiguration(
                            properties.getProperty("S3CloudStore.serviceEndPoint"), properties.getProperty("S3CloudStore.signingRegion")))
                    .build();
            log.info("S3Client created and connected to cloudStore");
        } catch (IOException e) {
            log.error("S3CloudStore.properties not found");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Article uploadText(CloudUploadRequest cloudUploadRequest) {
        try {
            byte[] articleContentBytes = cloudUploadRequest.getArticleContent().getBytes();
            String bucket = cloudUploadRequest.getBucketName();
            String key = cloudUploadRequest.getArticleId().toString();
            s3Client.putObject(new PutObjectRequest(bucket, key, new ByteArrayInputStream(articleContentBytes), articleContentSize(articleContentBytes)));
            log.info("The article '" + cloudUploadRequest.getArticleName() + "' by user '" + cloudUploadRequest.getUserOwner().getUsername() + "' was uploaded in the YandexCloudStore successfully");
            return cloudUploadRequestMapperWithUrlArticle(cloudUploadRequest, s3Client.getUrl(bucket, key.toString()).toString());
        } catch (AmazonS3Exception amazonS3Exception) {
            throw new CloudArticleTextDoesntCreatedRuntimeExceptions(cloudUploadRequest.getArticleName(), amazonS3Exception.getMessage());
        } catch (NullPointerException nullPointerException) {
            throw new CloudArticleTextDoesntCreatedRuntimeExceptions(cloudUploadRequest.getArticleName(), "CloudUploadRequest is wrong");
        }
    }

    @Override
    public String getArticleTextByUrl(String url) {
        UrlParser urlParser = new UrlParser();
        urlParser.parseUrl(url);
        String bucket = urlParser.getBucket();
        String key = urlParser.getKey();
        return getTextToString(bucket, key);
    }

    @Override
    public void updateText(String url, String newArticleContent) {
        try {
            UrlParser urlParser = new UrlParser();
            urlParser.parseUrl(url);
            byte[] newArticleContentBytes = newArticleContent.getBytes();
            s3Client.putObject(new PutObjectRequest(urlParser.getBucket(), urlParser.getKey(), new ByteArrayInputStream(newArticleContentBytes), articleContentSize(newArticleContentBytes)));
        } catch (AmazonS3Exception | NullPointerException exception) {
            log.error(exception.getMessage());
            throw new CloudArticleTextDoesntUpdateRuntimeException(exception.getMessage());
        }
    }

    @Override
    public void deleteArticle(String url) {
        try {
            UrlParser urlParser = new UrlParser();
            urlParser.parseUrl(url);
            String bucketName = urlParser.getBucket();
            String key = urlParser.getKey();
            s3Client.deleteObject(bucketName, key);
            log.info("Article by key: '" + key + "' deleted successfully");
        } catch (AmazonS3Exception | NullPointerException exception) {
            log.error("Article wasn't delete");
            throw new CloudArticleTextDoesntDeleteRuntimeException(exception.getMessage());
        }
    }

    private ObjectMetadata articleContentSize(byte[] articleContentBytes) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(articleContentBytes.length);
        return objectMetadata;
    }

    private Article cloudUploadRequestMapperWithUrlArticle(CloudUploadRequest cloudUploadRequest, String url) {
        Article article = new Article();
        article.setId(cloudUploadRequest.getArticleId());
        article.setNameArticle(cloudUploadRequest.getArticleName());
        article.setLikes(cloudUploadRequest.getLikes());
        article.setUserOwner(cloudUploadRequest.getUserOwner());
        article.setDateOfCreation(cloudUploadRequest.getDateOfCreation());
        article.setUrl(url);
        return article;
    }

    private String getTextToString(String bucketName, String key) {
        try {
            S3Object s3Object = s3Client.getObject(bucketName, key);
            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
            return IOUtils.toString(s3ObjectInputStream);
        } catch (IOException ioException) {
            throw new CloudConvertArticleTextContentToStringRuntimeException(ioException.getMessage());
        } catch (AmazonS3Exception amazonS3Exception) {
            throw new CloudArticleTextDoesntCanReadRuntimeException(amazonS3Exception.getMessage());
        }
    }
}


