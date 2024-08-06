package com.denisvasilenko.blogapp.cloudstore;

import com.denisvasilenko.blogapp.cloudstore.dto.CloudUploadRequest;
import com.denisvasilenko.blogapp.models.Article;

public interface CloudStoreInterface {

    Article uploadText(CloudUploadRequest cloudUploadRequest);

    String getArticleTextByUrl(String url);

    void updateText(String url, String newArticleContent);

    void deleteArticle(String url);
}
