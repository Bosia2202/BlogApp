package com.denisvasilenko.blogapp.cloudStore.DTO;

import com.denisvasilenko.blogapp.models.User;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class CloudUploadRequest {
    private final String bucketName;
    private final UUID articleId;
    private final String articleName;
    private final String articleContent;
    private final User userOwner;
    private final LocalDate dateOfCreation;
    private final int likes;

    public CloudUploadRequest(String bucketName, String articleName, String articleContent, User userOwner, LocalDate dateOfCreation) {
        this.bucketName = bucketName;
        this.articleId = UUID.randomUUID();
        this.articleName = articleName;
        this.articleContent = articleContent;
        this.userOwner = userOwner;
        this.dateOfCreation = dateOfCreation;
        this.likes = 0;
    }
}
