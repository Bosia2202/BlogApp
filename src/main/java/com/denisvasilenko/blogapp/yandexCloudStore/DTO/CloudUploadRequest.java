package com.denisvasilenko.blogapp.yandexCloudStore.DTO;

import com.denisvasilenko.blogapp.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;

@AllArgsConstructor
@Getter
public class CloudUploadRequest {
    private String bucketName;
    private String key;
    private String articleName;
    private String articleContent;
    private User userOwner;
    private Date DateOfCreation;
    private final int likes = 0;
}
