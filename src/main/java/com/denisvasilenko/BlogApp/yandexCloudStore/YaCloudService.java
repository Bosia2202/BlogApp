package com.denisvasilenko.BlogApp.yandexCloudStore;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class YaCloudService{
    private final AmazonS3 s3Client;

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
        log.info("S3Client created and connected to YandexObject");
    }

    public String uploadFiles(String bucketName,String key,String ArticleContent){
        byte[] contentBytes = ArticleContent.getBytes();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentBytes.length);
        PutObjectRequest request = new PutObjectRequest(bucketName, key, new ByteArrayInputStream(contentBytes), metadata);
        s3Client.putObject(request);
        log.info("Text "+key+" uploaded successfully");
        return s3Client.getUrl(bucketName,key).toString();
    }
    public String getArticleText(String url){
        try {
            UrlParser urlParser=new UrlParser(url);
            String bucketName = urlParser.getBucket();
            String key=urlParser.getKey();
            S3Object s3Object = s3Client.getObject(bucketName, key);
            log.debug("Get file");
            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
            String ObjContent = IOUtils.toString(s3ObjectInputStream);
            log.info("Article "+key+" content got and return");
            return ObjContent;
        }
        catch (IOException e){
            log.error("Failed to convert to String" +e);
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
            log.info("Article deleted:"+key);
        }
        catch (AmazonS3Exception e) {
            e.printStackTrace();
        }
    }
}


