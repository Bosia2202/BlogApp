package com.denisvasilenko.blogapp.yandexCloudStore;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.denisvasilenko.blogapp.exceptions.cloud.*;
import com.denisvasilenko.blogapp.models.Article;
import com.denisvasilenko.blogapp.yandexCloudStore.DTO.CloudUploadRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
@Log4j2
public class YaCloudService {
    private final AmazonS3 s3Client;

    public YaCloudService() {
        AWSCredentials credentials = new BasicAWSCredentials("YCAJExVGFzxJoYQ3X5rDuRWOX", "YCMqgcw71r3382pOr0m3wo-Y77gsdXXhZvSTdofp");
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AmazonS3ClientBuilder.EndpointConfiguration("storage.yandexcloud.net", "ru-central1"))
                .build();
        log.info("S3Client created and connected to YandexObject");
    }

    public Article uploadText(CloudUploadRequest cloudUploadRequest) {
        try {
            byte[] articleContentBytes = cloudUploadRequest.getArticleContent().getBytes();
            String bucket = cloudUploadRequest.getBucketName();
            String key = cloudUploadRequest.getArticleId().toString();
            s3Client.putObject(new PutObjectRequest(bucket,key, new ByteArrayInputStream(articleContentBytes), articleContentSize(articleContentBytes)));
            log.info("The article '" + cloudUploadRequest.getArticleName() + "' by user '" +cloudUploadRequest.getUserOwner().getUsername()+ "' was uploaded in the YandexCloudStore successfully");
            return cloudUploadRequestMapperWithUrlArticle(cloudUploadRequest, s3Client.getUrl(bucket,key.toString()).toString());
        }
        catch (AmazonS3Exception amazonS3Exception) {
            throw new CloudArticleTextDoesntCreatedRuntimeExceptions(cloudUploadRequest.getArticleName(),amazonS3Exception.getMessage());
        } catch (NullPointerException nullPointerException) {
            throw new CloudArticleTextDoesntCreatedRuntimeExceptions(cloudUploadRequest.getArticleName(),"CloudUploadRequest is wrong");
        }
    }

    public String getArticleTextByUrl(String url) {
        UrlParser urlParser = new UrlParser();
        urlParser.parseUrl(url);
        String bucket = urlParser.getBucket();
        String key = urlParser.getKey();
        return getTextToString(bucket,key);
    }

    public void updateText(String url,String newArticleContent) {
      try {
          UrlParser urlParser = new UrlParser();
          urlParser.parseUrl(url);
          byte[] newArticleContentBytes = newArticleContent.getBytes();
          s3Client.putObject(new PutObjectRequest(urlParser.getBucket(), urlParser.getKey(), new ByteArrayInputStream(newArticleContentBytes), articleContentSize(newArticleContentBytes)));
      }
      catch (AmazonS3Exception | NullPointerException exception) {
          log.error(exception.getMessage());
          throw new CloudArticleTextDoesntUpdateRuntimeException(exception.getMessage());
      }
    }

    public void deleteArticle(String url) {
        try {
            UrlParser urlParser = new UrlParser();
            urlParser.parseUrl(url);
            String bucketName = urlParser.getBucket();
            String key = urlParser.getKey();
            s3Client.deleteObject(bucketName, key);
            log.info("Article by key: '"+key+"' deleted successfully");
        }
        catch (AmazonS3Exception | NullPointerException exception) {
            log.error("Article wasn't delete");
            throw new CloudArticleTextDoesntDeleteRuntimeException(exception.getMessage());
        }
    }

    private ObjectMetadata articleContentSize(byte[] articleContentBytes) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(articleContentBytes.length);
        return objectMetadata;
    }

    private Article cloudUploadRequestMapperWithUrlArticle(CloudUploadRequest cloudUploadRequest,String url) {
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


