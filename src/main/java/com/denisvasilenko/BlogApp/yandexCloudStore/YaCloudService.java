package com.denisvasilenko.BlogApp.yandexCloudStore;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.denisvasilenko.BlogApp.models.Article;
import com.denisvasilenko.BlogApp.repositories.ArticleRepository;
import com.denisvasilenko.BlogApp.yandexCloudStore.DTO.CloudUploadRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class YaCloudService{
    private final AmazonS3 s3Client;
    public YaCloudService() {
        AWSCredentials credentials = new BasicAWSCredentials("YCAJExVGFzxJoYQ3X5rDuRWOX", "YCMqgcw71r3382pOr0m3wo-Y77gsdXXhZvSTdofp");
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AmazonS3ClientBuilder.EndpointConfiguration("storage.yandexcloud.net", "ru-central1"))
                .build();
        log.info("S3Client created and connected to YandexObject");
    }

    public Optional<Article> uploadFile(CloudUploadRequest cloudUploadRequest) {
        try {
            byte[] articleContentBytes = cloudUploadRequest.getArticleContent().getBytes();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(articleContentBytes.length);
            s3Client.putObject(new PutObjectRequest(cloudUploadRequest.getBucketName(),
                    cloudUploadRequest.getKey(), new ByteArrayInputStream(articleContentBytes), metadata));
            log.info("Text '" + cloudUploadRequest.getKey() + "' uploaded successfully");
            return Optional.of(cloudUploadRequestMapperWithUrlOnArticle(cloudUploadRequest,
                    s3Client.getUrl(cloudUploadRequest.getBucketName(),cloudUploadRequest.getKey()).toString()));
        }
        catch (AmazonS3Exception amazonS3Exception) {
            if (amazonS3Exception.getErrorCode().equals("NoSuchBucket")) {
                log.error("Bucket '"+cloudUploadRequest.getBucketName()+"' doesn't exists");
            }
            else {
                log.error(amazonS3Exception.getMessage());
            }
            return Optional.empty();
        }
        catch (NullPointerException nullPointerException) {
            log.error("Wrong arguments in the uploadFile"+nullPointerException.getMessage());
            return Optional.empty();
        }
    }
    private Article cloudUploadRequestMapperWithUrlOnArticle(CloudUploadRequest cloudUploadRequest,String url) {
        Article article = new Article();
        article.setNameArticle(cloudUploadRequest.getArticleName());
        article.setLikes(cloudUploadRequest.getLikes());
        article.setUserOwner(cloudUploadRequest.getUserOwner());
        article.setDateOfCreation(cloudUploadRequest.getDateOfCreation());
        article.setUrl(url);
        return article;
    }

    public String getArticleText(String url){
        try {
            UrlParser urlParser=new UrlParser(url);
            String bucketName = urlParser.getBucket();
            String key=urlParser.getKey();
            S3Object s3Object = s3Client.getObject(bucketName, key);
            //log.debug("Get file");
            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
            String ObjContent = IOUtils.toString(s3ObjectInputStream);
            //log.info("Article "+key+" content got and return");
            return ObjContent;
        }
        catch (IOException e){
            //log.error("Failed to convert to String" +e);
            return null;
        }
    }

   public List<String> getAllArticles(String bucketName){
        ListObjectsRequest listObjects =new ListObjectsRequest().withBucketName(bucketName).withDelimiter("/").withMaxKeys(300);
        ObjectListing objectListing=s3Client.listObjects(listObjects);
       List<String> allArticlesName = new ArrayList<>();
        for (S3ObjectSummary objectSummary:objectListing.getObjectSummaries()){
            allArticlesName.add(objectSummary.getKey());
        }
        return allArticlesName;
   }

    public void deleteArticle(String url) {
        try {
            UrlParser urlParser=new UrlParser(url);
            String bucketName=urlParser.getBucket();
            String key=urlParser.getKey();
            s3Client.deleteObject(bucketName, key);
          //  log.info("Article deleted:"+key);
        }
        catch (AmazonS3Exception e) {
            e.printStackTrace();
        }
    }
}


